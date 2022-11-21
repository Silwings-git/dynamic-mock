package top.silwings.admin.service.impl;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.repository.db.mysql.converter.MockHandlerDaoConverter;
import top.silwings.admin.repository.db.mysql.mapper.MockHandlerMapper;
import top.silwings.admin.repository.db.mysql.mapper.MockHandlerUniqueMapper;
import top.silwings.admin.repository.db.mysql.po.MockHandlerPo;
import top.silwings.admin.repository.db.mysql.po.MockHandlerUniquePo;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.model.dto.MockHandlerDto;
import top.silwings.core.model.dto.QueryConditionDto;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerApplication
 * @Description
 * @Author Silwings
 * @Date 2022/11/16 21:49
 * @Since
 **/
@Service
public class MockHandlerServiceImpl implements MockHandlerService {

    private final MockHandlerManager mockHandlerManager;

    private final MockHandlerFactory mockHandlerFactory;

    private final MockHandlerMapper mockHandlerMapper;

    private final MockHandlerUniqueMapper mockHandlerUniqueMapper;

    private final MockHandlerDaoConverter mockHandlerDaoConverter;


    public MockHandlerServiceImpl(final MockHandlerManager mockHandlerManager, final MockHandlerFactory mockHandlerFactory, final MockHandlerMapper mockHandlerMapper, final MockHandlerUniqueMapper mockHandlerUniqueMapper, final MockHandlerDaoConverter mockHandlerDaoConverter) {
        this.mockHandlerManager = mockHandlerManager;
        this.mockHandlerFactory = mockHandlerFactory;
        this.mockHandlerMapper = mockHandlerMapper;
        this.mockHandlerUniqueMapper = mockHandlerUniqueMapper;
        this.mockHandlerDaoConverter = mockHandlerDaoConverter;
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
            throw DynamicMockAdminException.from("Request path already exists.");
        }

        return Identity.from(mockHandlerPo.getHandlerId());
    }

    @Override
    @Transactional
    public Identity updateById(final MockHandlerDto mockHandlerDto) {

        final Identity handlerId = mockHandlerDto.getHandlerId();

        final MockHandlerPo mockHandlerPo = this.mockHandlerDaoConverter.convert(mockHandlerDto);

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
        this.mockHandlerUniqueMapper.insertList(uniqueList);

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

        final List<MockHandlerPo> mockHandlerPoList = this.mockHandlerMapper.selectByConditionAndRowBounds(example, new RowBounds(0, 1));

        CheckUtils.isNotEmpty(mockHandlerPoList, () -> DynamicMockAdminException.from("MockHandler does not exist."));

        return Identity.from(mockHandlerPoList.get(0).getProjectId());
    }

    @Override

    public PageData<MockHandlerDto> query(final QueryConditionDto queryCondition, final PageParam pageParam) {

        final Example condition = new Example(MockHandlerPo.class);
        final Example.Criteria criteria = condition.createCriteria();
        criteria
                .andEqualTo(MockHandlerPo.C_PROJECT_ID, queryCondition.getProjectId())
                .andLike(MockHandlerPo.C_NAME, ConvertUtils.getNoNullOrDefault(queryCondition.getName(), null, name -> "%" + name + "%"))
                .andLike(MockHandlerPo.C_REQUEST_URI, ConvertUtils.getNoNullOrDefault(queryCondition.getRequestUri(), null, uri -> "%" + uri + "%"))
                .andLike(MockHandlerPo.C_LABEL, ConvertUtils.getNoNullOrDefault(queryCondition.getLabel(), null, label -> "%" + label + "%"))
                .andEqualTo(MockHandlerPo.C_ENABLE_STATUS, ConvertUtils.getNoNullOrDefault(queryCondition.getEnableStatus(), null, EnableStatus::code));

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
    public void updateEnableStatus(final Identity handlerId, final EnableStatus enableStatus) {

        final MockHandlerPo mockHandler = new MockHandlerPo();
        mockHandler.setEnableStatus(enableStatus.code());

        final Example enableCondition = new Example(MockHandlerPo.class);
        enableCondition.createCriteria()
                .andEqualTo(MockHandlerPo.C_HANDLER_ID, handlerId.intValue());

        this.mockHandlerMapper.updateByConditionSelective(mockHandler, enableCondition);

        if (EnableStatus.ENABLE.equals(enableStatus)) {

            final MockHandlerDto mockHandlerDto = this.find(handlerId);

            this.mockHandlerManager.registerHandler(this.mockHandlerFactory.buildMockHandler(mockHandlerDto));

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

}