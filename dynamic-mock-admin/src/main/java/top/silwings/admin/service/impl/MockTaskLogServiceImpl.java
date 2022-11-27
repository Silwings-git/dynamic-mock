package top.silwings.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.repository.converter.MockTaskLogDaoConverter;
import top.silwings.admin.repository.mapper.MockTaskLogMapper;
import top.silwings.admin.repository.po.MockTaskLogPo;
import top.silwings.admin.service.MockTaskLogService;
import top.silwings.core.common.Identity;
import top.silwings.core.event.MockTaskEndEvent;
import top.silwings.core.event.MockTaskEvent;
import top.silwings.core.event.MockTaskStartEvent;
import top.silwings.core.model.MockTaskLogDto;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockTaskLogServiceImpl
 * @Description
 * @Author Silwings
 * @Date 2022/11/23 23:30
 * @Since
 **/
@Slf4j
@Service
public class MockTaskLogServiceImpl implements MockTaskLogService, ApplicationListener<MockTaskEvent> {

    private final MockTaskLogMapper mockTaskLogMapper;

    private final MockTaskLogDaoConverter mockTaskLogDaoConverter;

    public MockTaskLogServiceImpl(final MockTaskLogMapper mockTaskLogMapper, final MockTaskLogDaoConverter mockTaskLogDaoConverter) {
        this.mockTaskLogMapper = mockTaskLogMapper;
        this.mockTaskLogDaoConverter = mockTaskLogDaoConverter;
    }

    @Override
    public Identity create(final MockTaskLogDto mockTaskLog) {

        final MockTaskLogPo mockTaskLogPo = this.mockTaskLogDaoConverter.convert(mockTaskLog);
        mockTaskLogPo.setLogId(null);

        this.mockTaskLogMapper.insertSelective(mockTaskLogPo);

        return Identity.from(mockTaskLogPo.getLogId());
    }

    @Override
    public Identity updateByLogId(final MockTaskLogDto mockTaskLog) {

        if (null == mockTaskLog.getLogId()) {
            return null;
        }

        final MockTaskLogPo mockTaskLogPo = this.mockTaskLogDaoConverter.convert(mockTaskLog);
        mockTaskLogPo.setLogId(null);

        final Example example = new Example(MockTaskLogPo.class);
        example.createCriteria()
                .andEqualTo(MockTaskLogPo.C_LOG_ID, mockTaskLog.getLogId().intValue());

        this.mockTaskLogMapper.updateByConditionSelective(mockTaskLogPo, example);

        return mockTaskLog.getLogId();
    }

    @Override
    public PageData<MockTaskLogDto> query(final List<Identity> handlerIdList, final String name, final String taskCode, final PageParam pageParam) {

        if (CollectionUtils.isEmpty(handlerIdList)) {
            return PageData.empty();
        }

        final List<Integer> handlerIds = handlerIdList.stream().map(Identity::intValue).collect(Collectors.toList());

        final Example example = new Example(MockTaskLogPo.class);
        example.createCriteria()
                .andIn(MockTaskLogPo.C_HANDLER_ID, handlerIds)
                .andEqualTo(MockTaskLogPo.C_TASK_CODE, ConvertUtils.getNoBlankOrDefault(taskCode, null))
                .andLike(MockTaskLogPo.C_NAME, ConvertUtils.getNoBlankOrDefault(name, null, arg -> "%" + arg + "%"));

        final int total = this.mockTaskLogMapper.selectCountByCondition(example);
        if (total <= 0) {
            return PageData.empty();
        }

        final List<MockTaskLogPo> mockTaskLogPoList = this.mockTaskLogMapper.selectByConditionAndRowBounds(example, pageParam.toRowBounds());
        final List<MockTaskLogDto> mockTaskLogList = mockTaskLogPoList.stream()
                .map(this.mockTaskLogDaoConverter::convert)
                .collect(Collectors.toList());

        return PageData.of(mockTaskLogList, total);
    }

    @Override
    public void delete(final Identity handlerId, final Identity logId) {

        final Example example = new Example(MockTaskLogPo.class);
        example.createCriteria()
                .andEqualTo(MockTaskLogPo.C_HANDLER_ID, handlerId.intValue())
                .andEqualTo(MockTaskLogPo.C_LOG_ID, ConvertUtils.getNoNullOrDefault(logId, null, Identity::intValue));

        this.mockTaskLogMapper.deleteByCondition(example);
    }

    @Override
    public MockTaskLogDto find(final Identity logId) {

        final Example example = new Example(MockTaskLogPo.class);
        example.createCriteria()
                .andEqualTo(MockTaskLogPo.C_LOG_ID, logId.intValue());

        final List<MockTaskLogPo> mockTaskLogPoList = this.mockTaskLogMapper.selectByConditionAndRowBounds(example, new RowBounds(0, 1));

        CheckUtils.isNotEmpty(mockTaskLogPoList, DynamicMockAdminException.supplier(ErrorCode.MOCK_TASK_LOG_NOT_EXIST));

        return this.mockTaskLogDaoConverter.convert(mockTaskLogPoList.get(0));
    }

    @Override
    public void onApplicationEvent(final MockTaskEvent event) {
        try {
            if (event instanceof MockTaskStartEvent) {
                final Identity logId = this.create(event.getMockTaskLog());
                event.getMockTaskLog().setLogId(logId);
            } else if (event instanceof MockTaskEndEvent) {
                this.updateByLogId(event.getMockTaskLog());
            }
        } catch (Exception e) {
            log.error("处理 MockTaskEvent 失败.", e);
        }
    }
}