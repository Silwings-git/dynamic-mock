package top.silwings.core.repository.db.mysql;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.common.PageData;
import top.silwings.core.common.PageParam;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.repository.MockHandlerRepository;
import top.silwings.core.repository.db.mysql.dao.MockHandlerDao;
import top.silwings.core.repository.db.mysql.dao.MockHandlerUniqueDao;
import top.silwings.core.repository.db.mysql.dao.converter.MockHandlerDaoConverter;
import top.silwings.core.repository.dto.MockHandlerDto;
import top.silwings.core.repository.dto.QueryConditionDto;
import top.silwings.core.utils.ConvertUtils;

import java.util.List;
import java.util.Objects;
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

        final MockHandlerDao mockHandlerDao = this.mockHandlerDaoConverter.convert(mockHandlerDto);
        mockHandlerDao.setHandlerId(null);

        final long handlerId = this.mockHandlerMapper.insertSelective(mockHandlerDao);

        final List<MockHandlerUniqueDao> uniqueList = mockHandlerDto.getHttpMethods().stream()
                .map(method -> MockHandlerUniqueDao.of(handlerId, mockHandlerDao.getRequestUri(), method.name()))
                .collect(Collectors.toList());
        this.mockHandlerUniqueMapper.insertList(uniqueList);

        return Identity.from(handlerId);
    }

    @Transactional
    @Override
    public Identity update(final MockHandlerDto mockHandlerDto) {

        final Identity handlerId = Objects.requireNonNull(mockHandlerDto.getHandlerId());

        final MockHandlerDao mockHandlerDao = this.mockHandlerDaoConverter.convert(mockHandlerDto);

        final Example updateCondition = new Example(MockHandlerDao.class);
        updateCondition.createCriteria()
                .andEqualTo(MockHandlerDao.C_HANDLER_ID, handlerId.getId());

        this.mockHandlerMapper.updateByConditionSelective(mockHandlerDao, updateCondition);

        // 删除唯一表该handler的数据,重新创建
        final Example deleteCondition = new Example(MockHandlerUniqueDao.class);
        deleteCondition.createCriteria()
                .andEqualTo(MockHandlerUniqueDao.C_HANDLER_ID, handlerId.getId());
        this.mockHandlerUniqueMapper.deleteByCondition(deleteCondition);

        final List<MockHandlerUniqueDao> uniqueList = mockHandlerDto.getHttpMethods().stream()
                .map(method -> MockHandlerUniqueDao.of(handlerId.getId(), mockHandlerDao.getRequestUri(), method.name()))
                .collect(Collectors.toList());
        this.mockHandlerUniqueMapper.insertList(uniqueList);

        return handlerId;
    }

    @Override
    public MockHandlerDto find(final Identity handlerId) {

        final MockHandlerDao findCondition = new MockHandlerDao();
        findCondition.setHandlerId(handlerId.getId());

        final MockHandlerDao mockHandlerDao = this.mockHandlerMapper.selectOne(findCondition);

        if (null == mockHandlerDao) {
            throw new DynamicMockException("handler不存在");
        }

        return this.mockHandlerDaoConverter.convert(mockHandlerDao);
    }

    @Override
    public PageData<MockHandlerDto> query(final QueryConditionDto queryCondition, final PageParam pageParam) {

        if (null != queryCondition.getHandlerIdList() && queryCondition.getHandlerIdList().isEmpty()) {
            return PageData.empty();
        }

        final Example condition = new Example(MockHandlerDao.class);
        final Example.Criteria criteria = condition.createCriteria();
        criteria
                .andLike(MockHandlerDao.C_NAME, ConvertUtils.getNoNullOrDefault(queryCondition.getName(), null, arg -> "%".concat(arg).concat("%")))
                .andLike(MockHandlerDao.C_REQUEST_URI, ConvertUtils.getNoNullOrDefault(queryCondition.getRequestUri(), null, arg -> arg.concat("%")))
                .andLike(MockHandlerDao.C_LABEL, ConvertUtils.getNoNullOrDefault(queryCondition.getLabel(), null, arg -> "%".concat(arg).concat("%")))
                .andEqualTo(MockHandlerDao.C_ENABLE_STATUS, ConvertUtils.getNoNullOrDefault(queryCondition.getEnableStatus(), null, EnableStatus::code));

        if (null != queryCondition.getHandlerIdList()) {
            criteria.andIn(MockHandlerDao.C_HANDLER_ID, queryCondition.getHandlerIdList());
        }

        final long total = this.mockHandlerMapper.selectCountByExample(condition);
        if (total <= 0) {
            return PageData.empty();
        }

        final List<MockHandlerDto> mockHandlerDtoList = this.mockHandlerMapper.selectByConditionAndRowBounds(condition, pageParam.toRowBounds())
                .stream()
                .map(this.mockHandlerDaoConverter::convert)
                .collect(Collectors.toList());

        return PageData.of(mockHandlerDtoList, total);
    }

    @Transactional
    @Override
    public void delete(final Identity handlerId) {

        final Example deleteHandlerCondition = new Example(MockHandlerDao.class);
        deleteHandlerCondition.createCriteria()
                .andEqualTo(MockHandlerDao.C_HANDLER_ID, handlerId);
        this.mockHandlerMapper.deleteByCondition(deleteHandlerCondition);

        final Example deleteUniqueCondition = new Example(MockHandlerUniqueDao.class);
        deleteUniqueCondition.createCriteria()
                .andEqualTo(MockHandlerUniqueDao.C_HANDLER_ID, handlerId);
        this.mockHandlerUniqueMapper.deleteByCondition(deleteUniqueCondition);
    }

    @Transactional
    @Override
    public void updateEnableStatus(final Identity handlerId, final EnableStatus enableStatus) {

        final MockHandlerDao mockHandler = new MockHandlerDao();
        mockHandler.setEnableStatus(enableStatus.code());

        final Example enableCondition = new Example(MockHandlerDao.class);
        enableCondition.createCriteria()
                .andEqualTo(MockHandlerDao.C_HANDLER_ID, handlerId.getId());

        this.mockHandlerMapper.updateByConditionSelective(mockHandler, enableCondition);
    }
}