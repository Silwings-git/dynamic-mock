package top.silwings.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.HandlerInfoDto;
import top.silwings.admin.model.ProjectDto;
import top.silwings.admin.model.TextFile;
import top.silwings.admin.repository.converter.MockHandlerDaoConverter;
import top.silwings.admin.repository.mapper.MockHandlerMapper;
import top.silwings.admin.repository.mapper.MockHandlerUniqueMapper;
import top.silwings.admin.repository.po.MockHandlerPo;
import top.silwings.admin.repository.po.MockHandlerUniquePo;
import top.silwings.admin.service.FileService;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.QueryConditionDto;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerApplication
 * @Description
 * @Author Silwings
 * @Date 2022/11/16 21:49
 * @Since
 **/
@Slf4j
@Service
public class MockHandlerServiceImpl implements MockHandlerService {

    private final MockHandlerManager mockHandlerManager;

    private final MockHandlerFactory mockHandlerFactory;

    private final MockHandlerMapper mockHandlerMapper;

    private final MockHandlerUniqueMapper mockHandlerUniqueMapper;

    private final MockHandlerDaoConverter mockHandlerDaoConverter;

    private final FileService fileService;

    public MockHandlerServiceImpl(final MockHandlerManager mockHandlerManager, final MockHandlerFactory mockHandlerFactory, final MockHandlerMapper mockHandlerMapper, final MockHandlerUniqueMapper mockHandlerUniqueMapper, final MockHandlerDaoConverter mockHandlerDaoConverter, final FileService fileService) {
        this.mockHandlerManager = mockHandlerManager;
        this.mockHandlerFactory = mockHandlerFactory;
        this.mockHandlerMapper = mockHandlerMapper;
        this.mockHandlerUniqueMapper = mockHandlerUniqueMapper;
        this.mockHandlerDaoConverter = mockHandlerDaoConverter;
        this.fileService = fileService;
    }

    @Override
    @Transactional
    public Identity create(final MockHandlerDto mockHandlerDto) {

        final MockHandlerPo mockHandlerPo = this.mockHandlerDaoConverter.convert(mockHandlerDto);
        mockHandlerPo.setHandlerId(null);

        this.mockHandlerMapper.insertSelective(mockHandlerPo);

        final List<MockHandlerUniquePo> uniqueList = mockHandlerDto.getHttpMethods().stream()
                .map(method -> MockHandlerUniquePo.of(mockHandlerPo.getHandlerId(), mockHandlerPo.getRequestUri(), method.name()))
                .collect(Collectors.toList());
        try {
            this.mockHandlerUniqueMapper.insertList(uniqueList);
        } catch (Exception e) {
            log.error("Mock Handler唯一表插入数据失败.", e);
            throw DynamicMockAdminException.from(ErrorCode.MOCK_HANDLER_DUPLICATE_REQUEST_PATH);
        }

        return Identity.from(mockHandlerPo.getHandlerId());
    }

    @Override
    @Transactional
    public Identity updateById(final MockHandlerDto mockHandlerDto) {

        final Identity handlerId = mockHandlerDto.getHandlerId();

        final MockHandlerPo mockHandlerPo = this.mockHandlerDaoConverter.convert(mockHandlerDto);
        // 更新后的Mock Handler需要重新注册
        mockHandlerPo.setEnableStatus(EnableStatus.DISABLE.code());

        final Example updateCondition = new Example(MockHandlerPo.class);
        updateCondition.createCriteria()
                .andEqualTo(MockHandlerPo.C_HANDLER_ID, handlerId.intValue());

        this.mockHandlerMapper.updateByConditionSelective(mockHandlerPo, updateCondition);

        // 删除唯一表该handler的数据,重新创建
        final Example deleteCondition = new Example(MockHandlerUniquePo.class);
        deleteCondition.createCriteria()
                .andEqualTo(MockHandlerUniquePo.C_HANDLER_ID, handlerId.intValue());
        this.mockHandlerUniqueMapper.deleteByCondition(deleteCondition);

        final List<MockHandlerUniquePo> uniqueList = mockHandlerDto.getHttpMethods().stream()
                .map(method -> MockHandlerUniquePo.of(handlerId.intValue(), mockHandlerPo.getRequestUri(), method.name()))
                .collect(Collectors.toList());
        try {
            this.mockHandlerUniqueMapper.insertList(uniqueList);
        } catch (Exception e) {
            log.error("Mock Handler唯一表插入数据失败.", e);
            throw DynamicMockAdminException.from(ErrorCode.MOCK_HANDLER_DUPLICATE_REQUEST_PATH);
        }

        // 更新成功后取消注册该handler
        this.mockHandlerManager.unregisterHandler(handlerId);

        return handlerId;
    }

    @Override
    public MockHandlerDto find(final Identity handlerId) {

        final MockHandlerPo findCondition = new MockHandlerPo();
        findCondition.setHandlerId(handlerId.intValue());

        final MockHandlerPo mockHandlerPo = this.mockHandlerMapper.selectOne(findCondition);

        if (null == mockHandlerPo) {
            throw new DynamicMockException("Mock handler does not exist: " + handlerId);
        }

        return this.mockHandlerDaoConverter.convert(mockHandlerPo);
    }

    @Override
    public Identity findProjectId(final Identity handlerId) {

        final Example example = new Example(MockHandlerPo.class);
        example.createCriteria()
                .andEqualTo(MockHandlerPo.C_HANDLER_ID, handlerId.intValue());

        example.selectProperties(MockHandlerPo.C_PROJECT_ID);

        final List<MockHandlerPo> mockHandlerPoList = this.mockHandlerMapper.selectByConditionAndRowBounds(example, PageParam.oneRow());

        CheckUtils.isNotEmpty(mockHandlerPoList, DynamicMockAdminException.supplier(ErrorCode.MOCK_HANDLER_NOT_EXIST));

        return Identity.from(mockHandlerPoList.get(0).getProjectId());
    }

    @Override
    public PageData<MockHandlerDto> query(final QueryConditionDto queryCondition, final PageParam pageParam) {

        if (CollectionUtils.isEmpty(queryCondition.getProjectIdList())) {
            return PageData.empty();
        }

        final Example condition = new Example(MockHandlerPo.class);
        final Example.Criteria criteria = condition.createCriteria();
        criteria
                .andIn(MockHandlerPo.C_PROJECT_ID, Identity.toInt(queryCondition.getProjectIdList()))
                .andEqualTo(MockHandlerPo.C_PROJECT_ID, ConvertUtils.getNoNullOrDefault(queryCondition.getProjectId(), null, Identity::intValue))
                .andLike(MockHandlerPo.C_NAME, ConvertUtils.getNoBlankOrDefault(queryCondition.getName(), null, name -> "%" + name + "%"))
                .andLike(MockHandlerPo.C_REQUEST_URI, ConvertUtils.getNoBlankOrDefault(queryCondition.getRequestUri(), null, uri -> "%" + uri + "%"))
                .andLike(MockHandlerPo.C_LABEL, ConvertUtils.getNoBlankOrDefault(queryCondition.getLabel(), null, label -> "%" + label + "%"))
                .andLike(MockHandlerPo.C_HTTP_METHODS, ConvertUtils.getNoBlankOrDefault(queryCondition.getHttpMethod(), null, label -> "%" + label + "%"))
                .andEqualTo(MockHandlerPo.C_ENABLE_STATUS, ConvertUtils.getNoNullOrDefault(queryCondition.getEnableStatus(), null, EnableStatus::code));

        condition.orderBy(MockHandlerPo.C_NAME).asc();

        return this.queryPageData(condition, pageParam.toRowBounds());
    }

    @Override
    @Transactional
    public void delete(final Identity handlerId) {

        final Example deleteHandlerCondition = new Example(MockHandlerPo.class);
        deleteHandlerCondition.createCriteria()
                .andEqualTo(MockHandlerPo.C_HANDLER_ID, handlerId.intValue());
        this.mockHandlerMapper.deleteByCondition(deleteHandlerCondition);

        final Example deleteUniqueCondition = new Example(MockHandlerUniquePo.class);
        deleteUniqueCondition.createCriteria()
                .andEqualTo(MockHandlerUniquePo.C_HANDLER_ID, handlerId.intValue());
        this.mockHandlerUniqueMapper.deleteByCondition(deleteUniqueCondition);

        this.mockHandlerManager.unregisterHandler(handlerId);
    }

    @Override
    public void updateEnableStatus(final Identity handlerId, final EnableStatus enableStatus, final ProjectDto project) {

        final MockHandlerPo mockHandler = new MockHandlerPo();
        mockHandler.setEnableStatus(enableStatus.code());

        final Example enableCondition = new Example(MockHandlerPo.class);
        enableCondition.createCriteria()
                .andEqualTo(MockHandlerPo.C_HANDLER_ID, handlerId.intValue());

        this.mockHandlerMapper.updateByConditionSelective(mockHandler, enableCondition);

        if (EnableStatus.ENABLE.equals(enableStatus)) {

            final MockHandlerDto mockHandlerDto = this.find(handlerId);

            this.registerHandler(mockHandlerDto, project);

        } else {

            this.mockHandlerManager.unregisterHandler(handlerId);
        }
    }

    @Override
    public int findMockHandlerQuantityByProject(final Identity projectId) {

        final Example example = new Example(MockHandlerPo.class);
        example.createCriteria()
                .andEqualTo(MockHandlerPo.C_PROJECT_ID, projectId.intValue());

        return this.mockHandlerMapper.selectCountByCondition(example);
    }

    @Override
    public PageData<MockHandlerDto> queryEnableHandlerList(final PageParam pageParam) {

        final Example queryCondition = new Example(MockHandlerPo.class);
        queryCondition.createCriteria()
                .andEqualTo(MockHandlerPo.C_ENABLE_STATUS, EnableStatus.ENABLE.code());

        return this.queryPageData(queryCondition, pageParam.toRowBounds());
    }

    private List<MockHandlerDto> queryEnableHandlerList(final Identity projectId) {

        final Example queryCondition = new Example(MockHandlerPo.class);
        queryCondition.createCriteria()
                .andEqualTo(MockHandlerPo.C_ENABLE_STATUS, EnableStatus.ENABLE.code())
                .andEqualTo(MockHandlerPo.C_PROJECT_ID, projectId.intValue());

        return this.mockHandlerMapper.selectByCondition(queryCondition).stream()
                .map(this.mockHandlerDaoConverter::convert)
                .collect(Collectors.toList());
    }


    private PageData<MockHandlerDto> queryPageData(final Example queryCondition, final RowBounds rowBounds) {

        final long total = this.mockHandlerMapper.selectCountByExample(queryCondition);
        if (total <= 0) {
            return PageData.empty();
        }

        final List<MockHandlerPo> mockHandlerList = this.mockHandlerMapper.selectByConditionAndRowBounds(queryCondition, rowBounds);

        final List<MockHandlerDto> mockHandlerDtoList = mockHandlerList
                .stream()
                .map(this.mockHandlerDaoConverter::convert)
                .collect(Collectors.toList());

        return PageData.of(mockHandlerDtoList, total);
    }


    /**
     * 根据项目id查询所属的全部处理器id
     *
     * @param projectId 项目id
     * @return 项目id下全部处理器id
     */
    @Override
    public List<Identity> queryHandlerIds(final Identity projectId) {

        if (null == projectId) {
            return Collections.emptyList();
        }

        final Example example = new Example(MockHandlerPo.class);
        example.createCriteria()
                .andEqualTo(MockHandlerPo.C_PROJECT_ID, projectId.intValue());

        example.selectProperties(MockHandlerPo.C_HANDLER_ID);

        return this.mockHandlerMapper.selectByCondition(example)
                .stream()
                .map(MockHandlerPo::getHandlerId)
                .map(Identity::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<Identity> queryHandlerIds(final List<Identity> projectIdList) {

        if (CollectionUtils.isEmpty(projectIdList)) {
            return Collections.emptyList();
        }

        final Example example = new Example(MockHandlerPo.class);
        example.createCriteria()
                .andIn(MockHandlerPo.C_PROJECT_ID, Identity.toInt(projectIdList));

        example.selectProperties(MockHandlerPo.C_HANDLER_ID);

        return this.mockHandlerMapper.selectByCondition(example)
                .stream()
                .map(MockHandlerPo::getHandlerId)
                .map(Identity::from)
                .collect(Collectors.toList());
    }

    @Override
    public void reRegisterHandler(final ProjectDto project) {

        final List<MockHandlerDto> mockHandlerList = this.queryEnableHandlerList(project.getProjectId());

        mockHandlerList.stream().map(MockHandlerDto::getHandlerId).forEach(this.mockHandlerManager::unregisterHandler);

        mockHandlerList.forEach(handler -> this.registerHandler(handler, project));
    }

    @Override
    public void registerHandler(final MockHandlerDto mockHandler, final ProjectDto project) {

        CheckUtils.isEquals(mockHandler.getProjectId(), project.getProjectId(), DynamicMockAdminException.supplier(ErrorCode.MOCK_HANDLER_PROJECT_MISMATCH, mockHandler.getProjectId().stringValue(), project.getProjectId().stringValue()));

        MockHandlerDto actualMockHandler;

        if (StringUtils.isNotBlank(project.getBaseUri())) {
            // 在原始的处理地址上添加项目基础uri
            actualMockHandler = MockHandlerDto.copyOf(mockHandler, (handler, builder) -> {
                builder.requestUri(project.getBaseUri() + mockHandler.getRequestUri());
                this.customizeSpaceFileProcessing(mockHandler);
            });
        } else {
            actualMockHandler = MockHandlerDto.copyOf(mockHandler, (handler, builder) -> this.customizeSpaceFileProcessing(mockHandler));
        }

        this.mockHandlerManager.registerHandler(this.mockHandlerFactory.buildMockHandler(actualMockHandler));
    }

    private void customizeSpaceFileProcessing(final MockHandlerDto mockHandler) {
        final Map<String, Object> customizeSpace = mockHandler.getCustomizeSpace();

        for (final Map.Entry<String, Object> entry : customizeSpace.entrySet()) {
            final Object value = entry.getValue();
            if (value instanceof String) {
                final String valueStr = (String) value;
                if (valueStr.startsWith(FILE_FLAG) && (valueStr.endsWith(".json") || valueStr.endsWith(".txt"))) {
                    final TextFile textFile = this.fileService.find(valueStr.replace(FILE_FLAG, "").trim());
                    CheckUtils.isTrue(JsonUtils.isValidJson(textFile.getContent()), DynamicMockAdminException.supplier(ErrorCode.CONTENT_FORMAT_ERROR));
                    customizeSpace.put(entry.getKey(), JsonUtils.toBean(textFile.getContent()));
                }
            }
        }
    }

    /**
     * 根据项目id查询handler信息.如果 projectIdList 为 null 表示查询所有
     *
     * @param projectIdList 项目id集
     */
    @Override
    public List<HandlerInfoDto> queryOwn(final List<Identity> projectIdList) {

        if (CollectionUtils.isEmpty(projectIdList)) {
            return Collections.emptyList();
        }

        final Example example = new Example(MockHandlerPo.class);
        example.createCriteria()
                .andIn(MockHandlerPo.C_PROJECT_ID, Identity.toInt(projectIdList));

        example.orderBy(MockHandlerPo.C_NAME).asc();

        example.selectProperties(MockHandlerPo.C_PROJECT_ID, MockHandlerPo.C_HANDLER_ID, MockHandlerPo.C_NAME);

        final List<MockHandlerPo> projectPoList = this.mockHandlerMapper.selectByCondition(example);

        return projectPoList.stream()
                .map(HandlerInfoDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public HandlerInfoDto findHandlerInfo(final Identity handlerId) {

        final Example example = new Example(MockHandlerPo.class);
        example.createCriteria()
                .andEqualTo(MockHandlerPo.C_HANDLER_ID, handlerId.intValue());

        example.selectProperties(MockHandlerPo.C_PROJECT_ID, MockHandlerPo.C_HANDLER_ID, MockHandlerPo.C_NAME);

        return this.mockHandlerMapper.selectByConditionAndRowBounds(example, PageParam.oneRow())
                .stream()
                .map(HandlerInfoDto::from)
                .findFirst()
                .orElse(null);
    }


}