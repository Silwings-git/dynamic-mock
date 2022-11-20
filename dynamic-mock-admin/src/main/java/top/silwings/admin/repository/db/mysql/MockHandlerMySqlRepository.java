package top.silwings.admin.repository.db.mysql;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.repository.MockHandlerRepository;
import top.silwings.admin.repository.db.mysql.converter.MockHandlerDaoConverter;
import top.silwings.admin.repository.db.mysql.mapper.MockHandlerMapper;
import top.silwings.admin.repository.db.mysql.mapper.MockHandlerUniqueMapper;
import top.silwings.admin.repository.db.mysql.po.MockHandlerPo;
import top.silwings.admin.repository.db.mysql.po.MockHandlerUniquePo;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.model.dto.MockHandlerDto;
import top.silwings.core.model.dto.QueryConditionDto;
import top.silwings.core.utils.ConvertUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerRepository
 * @Description
 * @Author Silwings
 * @Date 2022/11/15 22:35
 * @Since
 **/
@Component
public class MockHandlerMySqlRepository implements MockHandlerRepository {

    private final MockHandlerMapper mockHandlerMapper;

    private final MockHandlerUniqueMapper mockHandlerUniqueMapper;

    private final MockHandlerDaoConverter mockHandlerDaoConverter;

    public MockHandlerMySqlRepository(final MockHandlerMapper mockHandlerMapper, final MockHandlerUniqueMapper mockHandlerUniqueMapper, final MockHandlerDaoConverter mockHandlerDaoConverter) {
        this.mockHandlerMapper = mockHandlerMapper;
        this.mockHandlerUniqueMapper = mockHandlerUniqueMapper;
        this.mockHandlerDaoConverter = mockHandlerDaoConverter;
    }

    @Transactional
    @Override
    public Identity create(final MockHandlerDto mockHandlerDto) {

        final MockHandlerPo mockHandlerPo = this.mockHandlerDaoConverter.convert(mockHandlerDto);
        mockHandlerPo.setHandlerId(null);

        this.mockHandlerMapper.insertSelective(mockHandlerPo);

        final List<MockHandlerUniquePo> uniqueList = mockHandlerDto.getHttpMethods().stream()
                .map(method -> MockHandlerUniquePo.of(mockHandlerPo.getHandlerId(), mockHandlerPo.getRequestUri(), method.name()))
                .collect(Collectors.toList());
        this.mockHandlerUniqueMapper.insertList(uniqueList);

        return Identity.from(mockHandlerPo.getHandlerId());
    }

    @Transactional
    @Override
    public Identity update(final MockHandlerDto mockHandlerDto) {

        final Identity handlerId = Objects.requireNonNull(mockHandlerDto.getHandlerId());

        final MockHandlerPo mockHandlerPo = this.mockHandlerDaoConverter.convert(mockHandlerDto);

        final Example updateCondition = new Example(MockHandlerPo.class);
        updateCondition.createCriteria()
                .andEqualTo(MockHandlerPo.C_HANDLER_ID, handlerId.longValue());

        this.mockHandlerMapper.updateByConditionSelective(mockHandlerPo, updateCondition);

        // 删除唯一表该handler的数据,重新创建
        final Example deleteCondition = new Example(MockHandlerUniquePo.class);
        deleteCondition.createCriteria()
                .andEqualTo(MockHandlerUniquePo.C_HANDLER_ID, handlerId.longValue());
        this.mockHandlerUniqueMapper.deleteByCondition(deleteCondition);

        final List<MockHandlerUniquePo> uniqueList = mockHandlerDto.getHttpMethods().stream()
                .map(method -> MockHandlerUniquePo.of(handlerId.longValue(), mockHandlerPo.getRequestUri(), method.name()))
                .collect(Collectors.toList());
        this.mockHandlerUniqueMapper.insertList(uniqueList);

        return handlerId;
    }

    @Override
    public MockHandlerDto find(final Identity handlerId) {

        final MockHandlerPo findCondition = new MockHandlerPo();
        findCondition.setHandlerId(handlerId.longValue());

        final MockHandlerPo mockHandlerPo = this.mockHandlerMapper.selectOne(findCondition);

        if (null == mockHandlerPo) {
            throw new DynamicMockException("Mock handler does not exist: " + handlerId);
        }

        return this.mockHandlerDaoConverter.convert(mockHandlerPo);
    }

    @Override
    public PageData<MockHandlerDto> query(final QueryConditionDto queryCondition, final PageParam pageParam) {

        if (null != queryCondition.getHandlerIdList() && queryCondition.getHandlerIdList().isEmpty()) {
            return PageData.empty();
        }

        final Example condition = new Example(MockHandlerPo.class);
        final Example.Criteria criteria = condition.createCriteria();
        criteria
                .andLike(MockHandlerPo.C_NAME, ConvertUtils.getNoNullOrDefault(queryCondition.getName(), null, arg -> "%".concat(arg).concat("%")))
                .andLike(MockHandlerPo.C_REQUEST_URI, ConvertUtils.getNoNullOrDefault(queryCondition.getRequestUri(), null, arg -> arg.concat("%")))
                .andLike(MockHandlerPo.C_LABEL, ConvertUtils.getNoNullOrDefault(queryCondition.getLabel(), null, arg -> "%".concat(arg).concat("%")))
                .andEqualTo(MockHandlerPo.C_ENABLE_STATUS, ConvertUtils.getNoNullOrDefault(queryCondition.getEnableStatus(), null, EnableStatus::code));

        if (null != queryCondition.getHandlerIdList()) {
            criteria.andIn(MockHandlerPo.C_HANDLER_ID, queryCondition.getHandlerIdList().stream().map(Identity::longValue).collect(Collectors.toList()));
        }

        return this.queryPageData(condition, pageParam.toRowBounds());
    }

    @Transactional
    @Override
    public boolean delete(final Identity handlerId) {

        final Example deleteHandlerCondition = new Example(MockHandlerPo.class);
        deleteHandlerCondition.createCriteria()
                .andEqualTo(MockHandlerPo.C_HANDLER_ID, handlerId.longValue());
        this.mockHandlerMapper.deleteByCondition(deleteHandlerCondition);

        final Example deleteUniqueCondition = new Example(MockHandlerUniquePo.class);
        deleteUniqueCondition.createCriteria()
                .andEqualTo(MockHandlerUniquePo.C_HANDLER_ID, handlerId.longValue());
        final int row = this.mockHandlerUniqueMapper.deleteByCondition(deleteUniqueCondition);
        return row > 0;
    }

    @Transactional
    @Override
    public void delete(Collection<Identity> handlerIdList) {

        if (CollectionUtils.isEmpty(handlerIdList)) {
            return;
        }

        final Set<Long> handlerIdSet = handlerIdList.stream().map(Identity::longValue).collect(Collectors.toSet());

        final Example deleteHandlerCondition = new Example(MockHandlerPo.class);
        deleteHandlerCondition.createCriteria()
                .andIn(MockHandlerPo.C_HANDLER_ID, handlerIdSet);
        this.mockHandlerMapper.deleteByCondition(deleteHandlerCondition);

        final Example deleteUniqueCondition = new Example(MockHandlerUniquePo.class);
        deleteUniqueCondition.createCriteria()
                .andIn(MockHandlerUniquePo.C_HANDLER_ID, handlerIdSet);
        this.mockHandlerUniqueMapper.deleteByCondition(deleteUniqueCondition);
    }

    @Override
    public void updateEnableStatus(final Identity handlerId, final EnableStatus enableStatus) {

        final MockHandlerPo mockHandler = new MockHandlerPo();
        mockHandler.setEnableStatus(enableStatus.code());

        final Example enableCondition = new Example(MockHandlerPo.class);
        enableCondition.createCriteria()
                .andEqualTo(MockHandlerPo.C_HANDLER_ID, handlerId.longValue());

        this.mockHandlerMapper.updateByConditionSelective(mockHandler, enableCondition);
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