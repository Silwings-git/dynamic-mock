package cn.silwings.admin.repository.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import cn.silwings.admin.common.enums.MockHandlerComponentType;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.admin.model.MockHandlerConditionRepository;
import cn.silwings.admin.repository.MockHandlerTaskRepository;
import cn.silwings.admin.repository.converter.MockHandlerTaskDaoConverter;
import cn.silwings.admin.repository.mapper.MockHandlerTaskMapper;
import cn.silwings.admin.repository.mapper.MockHandlerTaskRequestMapper;
import cn.silwings.admin.repository.po.MockHandlerConditionPo;
import cn.silwings.admin.repository.po.MockHandlerTaskPo;
import cn.silwings.admin.repository.po.MockHandlerTaskRequestPo;
import cn.silwings.admin.repository.po.pack.MockHandlerTaskPoWrap;
import cn.silwings.core.common.EnableStatus;
import cn.silwings.core.common.Identity;
import cn.silwings.core.model.TaskInfoDto;
import cn.silwings.core.utils.CheckUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerTaskRepositoryImpl
 * @Description
 * @Author Silwings
 * @Date 2023/8/7 15:20
 * @Since
 **/
@Component
public class MockHandlerTaskRepositoryImpl implements MockHandlerTaskRepository {

    private final MockHandlerConditionRepository mockHandlerConditionRepository;

    private final MockHandlerTaskMapper mockHandlerTaskMapper;

    private final MockHandlerTaskRequestMapper mockHandlerTaskRequestMapper;

    private final MockHandlerTaskDaoConverter mockHandlerTaskDaoConverter;

    public MockHandlerTaskRepositoryImpl(final MockHandlerConditionRepository mockHandlerConditionRepository, final MockHandlerTaskMapper mockHandlerTaskMapper, final MockHandlerTaskRequestMapper mockHandlerTaskRequestMapper, final MockHandlerTaskDaoConverter mockHandlerTaskDaoConverter) {
        this.mockHandlerConditionRepository = mockHandlerConditionRepository;
        this.mockHandlerTaskMapper = mockHandlerTaskMapper;
        this.mockHandlerTaskRequestMapper = mockHandlerTaskRequestMapper;
        this.mockHandlerTaskDaoConverter = mockHandlerTaskDaoConverter;
    }

    @Override
    public List<TaskInfoDto> queryMockHandlerTaskList(final Identity handlerId) {

        final Example taskExample = new Example(MockHandlerTaskPo.class);
        taskExample.createCriteria()
                .andEqualTo(MockHandlerTaskPo.C_HANDLER_ID, handlerId.intValue());

        return this.mockHandlerTaskMapper.selectByCondition(taskExample)
                .stream()
                .sorted(Comparator.comparingInt(MockHandlerTaskPo::getSort))
                .map(mockHandlerTaskPo -> {
                    final Identity taskId = Identity.from(mockHandlerTaskPo.getTaskId());

                    // 条件信息
                    final List<MockHandlerConditionPo> mockHandlerConditionPoList = this.mockHandlerConditionRepository.queryConditions(handlerId, taskId, MockHandlerComponentType.MOCK_HANDLER_TASK);

                    // 请求信息
                    final MockHandlerTaskRequestPo taskRequestPo = this.findTaskRequest(handlerId, taskId);

                    return this.mockHandlerTaskDaoConverter.convert(mockHandlerTaskPo, mockHandlerConditionPoList, taskRequestPo);
                })
                .collect(Collectors.toList());
    }

    private MockHandlerTaskRequestPo findTaskRequest(final Identity handlerId, final Identity taskId) {
        return this.mockHandlerTaskRequestMapper.selectOne(new MockHandlerTaskRequestPo().setHandlerId(handlerId.intValue()).setTaskId(taskId.intValue()));
    }

    @Override
    @Transactional
    public void insertMockHandlerTask(final List<MockHandlerTaskPoWrap> mockHandlerTaskPoWrapList) {

        mockHandlerTaskPoWrapList.forEach(mockHandlerTaskPoWrap -> {
            final MockHandlerTaskPo mockHandlerTaskPo = mockHandlerTaskPoWrap.getMockHandlerTaskPo();
            this.mockHandlerTaskMapper.insertSelective(mockHandlerTaskPo);

            final List<MockHandlerConditionPo> mockHandlerConditionPoList = mockHandlerTaskPoWrap.getMockHandlerConditionPoList();
            mockHandlerConditionPoList.forEach(e ->
                    this.mockHandlerConditionRepository.insertSelective(Identity.from(mockHandlerTaskPo.getHandlerId()), Identity.from(mockHandlerTaskPo.getTaskId()), MockHandlerComponentType.MOCK_HANDLER_TASK, e));

            final MockHandlerTaskRequestPo mockHandlerTaskRequestPo = mockHandlerTaskPoWrap.getMockHandlerTaskRequestPo();
            mockHandlerTaskRequestPo.setHandlerId(mockHandlerTaskPo.getHandlerId());
            mockHandlerTaskRequestPo.setTaskId(mockHandlerTaskPo.getTaskId());
            this.mockHandlerTaskRequestMapper.insertSelective(mockHandlerTaskRequestPo);
        });
    }

    @Override
    @Transactional
    public boolean deleteMockHandlerTask(final Identity handlerId) {
        final Example taskExample = new Example(MockHandlerTaskPo.class);
        taskExample.createCriteria()
                .andEqualTo(MockHandlerTaskPo.C_HANDLER_ID, handlerId.intValue());
        this.mockHandlerTaskMapper.deleteByCondition(taskExample);

        this.mockHandlerConditionRepository.delete(handlerId, MockHandlerComponentType.MOCK_HANDLER_TASK);

        final Example requestExample = new Example(MockHandlerTaskRequestPo.class);
        requestExample.createCriteria()
                .andEqualTo(MockHandlerTaskRequestPo.C_HANDLER_ID, handlerId.intValue());
        this.mockHandlerTaskRequestMapper.deleteByCondition(requestExample);

        return true;
    }

    @Override
    public void updateTaskEnableStatus(final Identity handlerId, final Identity taskId, final EnableStatus enableStatus) {

        final Example example = new Example(MockHandlerTaskPo.class);
        example.createCriteria()
                .andEqualTo(MockHandlerTaskPo.C_HANDLER_ID, handlerId.intValue())
                .andEqualTo(MockHandlerTaskPo.C_TASK_ID, taskId.intValue());

        final MockHandlerTaskPo newTaskStatus = new MockHandlerTaskPo();
        newTaskStatus.setEnableStatus(enableStatus.code());

        final int row = this.mockHandlerTaskMapper.updateByConditionSelective(newTaskStatus, example);
        CheckUtils.isTrue(row > 0, DynamicMockAdminException.supplier(ErrorCode.MOCK_HANDLER_TASK_NOT_EXIST));
    }
}