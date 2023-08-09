package top.silwings.admin.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.MockHandlerDefineSnapshotDto;
import top.silwings.admin.repository.MockHandlerDefineSnapshotRepository;
import top.silwings.admin.repository.converter.MockHandlerDefineSnapshotDaoConverter;
import top.silwings.admin.repository.mapper.MockHandlerDefineSnapshotMapper;
import top.silwings.admin.repository.po.MockHandlerDefineSnapshotPo;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.utils.ConvertUtils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerDefineSnapshotServiceImpl
 * @Description
 * @Author Silwings
 * @Date 2023/8/9 15:25
 * @Since
 **/
@Slf4j
@Service
public class MockHandlerDefineSnapshotRepositoryImpl implements MockHandlerDefineSnapshotRepository {

    private final MockHandlerDefineSnapshotMapper mockHandlerDefineSnapshotMapper;

    private final MockHandlerDefineSnapshotDaoConverter mockHandlerDefineSnapshotDaoConverter;

    public MockHandlerDefineSnapshotRepositoryImpl(final MockHandlerDefineSnapshotMapper mockHandlerDefineSnapshotMapper, final MockHandlerDefineSnapshotDaoConverter mockHandlerDefineSnapshotDaoConverter) {
        this.mockHandlerDefineSnapshotMapper = mockHandlerDefineSnapshotMapper;
        this.mockHandlerDefineSnapshotDaoConverter = mockHandlerDefineSnapshotDaoConverter;
    }

    @Override
    @Transactional
    public void snapshot(final Identity handlerId, final MockHandlerDto mockHandlerDto) {

        // 如果mockHandlerDto中包含了handlerId,则其应该等于参数传递的handlerId
        if (null != mockHandlerDto.getHandlerId() && !mockHandlerDto.getHandlerId().equals(handlerId)) {
            throw DynamicMockAdminException.of(ErrorCode.VALID_ERROR, "handlerId");
        }

        final MockHandlerDto actMockHandler = null == mockHandlerDto.getHandlerId()
                ? MockHandlerDto.copyOf(mockHandlerDto, (handler, builder) -> builder.handlerId(handlerId))
                : mockHandlerDto;

        final String version = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        final MockHandlerDefineSnapshotPo mockHandlerDefineSnapshotPo = this.mockHandlerDefineSnapshotDaoConverter.convert(handlerId, version, actMockHandler, UserHolder.getUserId());

        this.mockHandlerDefineSnapshotMapper.insertSelective(mockHandlerDefineSnapshotPo);
    }

    @Override
    public MockHandlerDefineSnapshotDto findLast(final String snapshotVersion) {

        if (StringUtils.isBlank(snapshotVersion)) {
            return null;
        }

        final Example example = new Example(MockHandlerDefineSnapshotPo.class);
        example.createCriteria()
                .andEqualTo(MockHandlerDefineSnapshotPo.C_SNAPSHOT_VERSION, ConvertUtils.getNoBlankOrDefault(snapshotVersion, ""));
        example.orderBy(MockHandlerDefineSnapshotPo.C_SNAPSHOT_ID).desc();

        final List<MockHandlerDefineSnapshotPo> mockHandlerDefineSnapshotPoList = this.mockHandlerDefineSnapshotMapper.selectByConditionAndRowBounds(example, PageParam.oneRow());

        if (CollectionUtils.isEmpty(mockHandlerDefineSnapshotPoList)) {
            return null;
        }

        return this.mockHandlerDefineSnapshotDaoConverter.convert(mockHandlerDefineSnapshotPoList.get(0));
    }

    @Override
    public PageData<MockHandlerDefineSnapshotDto> query(final Collection<Identity> handlerIds, final PageParam pageParam) {

        final Example example = new Example(MockHandlerDefineSnapshotPo.class);
        example.createCriteria()
                .andIn(MockHandlerDefineSnapshotPo.C_HANDLER_ID, Identity.toInt(handlerIds));
        example.orderBy(MockHandlerDefineSnapshotPo.C_SNAPSHOT_ID).desc();

        final int total = this.mockHandlerDefineSnapshotMapper.selectCountByCondition(example);
        if (total <= 0) {
            return PageData.empty();
        }

        final List<MockHandlerDefineSnapshotPo> mockHandlerDefineSnapshotPoList = this.mockHandlerDefineSnapshotMapper.selectByConditionAndRowBounds(example, pageParam.toRowBounds());

        final List<MockHandlerDefineSnapshotDto> dataList = mockHandlerDefineSnapshotPoList.stream()
                .map(this.mockHandlerDefineSnapshotDaoConverter::convert)
                .collect(Collectors.toList());

        return PageData.of(dataList, total);
    }
}