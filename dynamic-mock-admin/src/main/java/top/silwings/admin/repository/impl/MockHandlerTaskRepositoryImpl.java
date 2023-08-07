package top.silwings.admin.repository.impl;

import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.enums.MockHandlerComponentType;
import top.silwings.admin.repository.MockHandlerTaskRepository;
import top.silwings.admin.repository.converter.MockHandlerTaskDaoConverter;
import top.silwings.admin.repository.mapper.ConditionMapper;
import top.silwings.admin.repository.mapper.MockHandlerTaskMapper;
import top.silwings.admin.repository.mapper.MockHandlerTaskRequestMapper;
import top.silwings.admin.repository.po.ConditionPo;
import top.silwings.admin.repository.po.MockHandlerTaskPo;
import top.silwings.admin.repository.po.MockHandlerTaskRequestPo;
import top.silwings.admin.repository.po.pack.MockHandlerTaskPoWrap;
import top.silwings.core.common.Identity;
import top.silwings.core.model.TaskInfoDto;

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

    private final ConditionMapper conditionMapper;

    private final MockHandlerTaskMapper mockHandlerTaskMapper;

    private final MockHandlerTaskRequestMapper mockHandlerTaskRequestMapper;

    private final MockHandlerTaskDaoConverter mockHandlerTaskDaoConverter;

    public MockHandlerTaskRepositoryImpl(final ConditionMapper conditionMapper, final MockHandlerTaskMapper mockHandlerTaskMapper, final MockHandlerTaskRequestMapper mockHandlerTaskRequestMapper, final MockHandlerTaskDaoConverter mockHandlerTaskDaoConverter) {
        this.conditionMapper = conditionMapper;
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
                .map(mockHandlerTaskPo -> {
                    final Identity taskId = Identity.from(mockHandlerTaskPo.getTaskId());

                    // 条件信息
                    final List<ConditionPo> conditionPoList = this.queryConditions(taskId);

                    // 请求信息
                    final MockHandlerTaskRequestPo taskRequestPo = this.findTaskRequest(taskId);

                    return this.mockHandlerTaskDaoConverter.convert(mockHandlerTaskPo, conditionPoList, taskRequestPo);
                })
                .collect(Collectors.toList());
    }

    private MockHandlerTaskRequestPo findTaskRequest(final Identity taskId) {
        return this.mockHandlerTaskRequestMapper.selectOne(new MockHandlerTaskRequestPo().setTaskId(taskId.intValue()));
    }

    private List<ConditionPo> queryConditions(final Identity taskId) {
        final Example conditionExample = new Example(ConditionPo.class);
        conditionExample.createCriteria()
                .andEqualTo(ConditionPo.C_COMPONENT_ID, taskId.intValue())
                .andEqualTo(ConditionPo.C_COMPONENT_TYPE, MockHandlerComponentType.MOCK_HANDLER_TASK);
        return this.conditionMapper.selectByCondition(conditionExample);
    }

    @Override
    public void insertMockHandlerTask(final List<MockHandlerTaskPoWrap> mockHandlerTaskPoWrapList) {

        mockHandlerTaskPoWrapList.forEach(mockHandlerTaskPoWrap -> {
            final MockHandlerTaskPo mockHandlerTaskPo = mockHandlerTaskPoWrap.getMockHandlerTaskPo();
            this.mockHandlerTaskMapper.insertSelective(mockHandlerTaskPo);

            final List<ConditionPo> conditionPoList = mockHandlerTaskPoWrap.getConditionPoList();
            conditionPoList.stream().forEach(e -> {
                e.setHandlerId(mockHandlerTaskPo.getHandlerId());
                e.setComponentId(mockHandlerTaskPo.getTaskId());
                e.setComponentType(MockHandlerComponentType.MOCK_HANDLER_TASK);
                this.conditionMapper.insertSelective(e);
            });

            final MockHandlerTaskRequestPo mockHandlerTaskRequestPo = mockHandlerTaskPoWrap.getMockHandlerTaskRequestPo();
            mockHandlerTaskRequestPo.setHandlerId(mockHandlerTaskPo.getHandlerId());
            this.mockHandlerTaskRequestMapper.insertSelective(mockHandlerTaskRequestPo);
        });
    }

    @Override
    public boolean removeMockHandlerTask(final Identity handlerId) {
        final Example taskExample = new Example(MockHandlerTaskPo.class);
        taskExample.createCriteria()
                .andEqualTo(MockHandlerTaskPo.C_HANDLER_ID, handlerId.intValue());
        this.mockHandlerTaskMapper.deleteByCondition(taskExample);

        final Example conditionExample = new Example(ConditionPo.class);
        taskExample.createCriteria()
                .andEqualTo(ConditionPo.C_HANDLER_ID, handlerId.intValue());
        this.conditionMapper.deleteByCondition(conditionExample);

        final Example requestExample = new Example(MockHandlerTaskRequestPo.class);
        taskExample.createCriteria()
                .andEqualTo(MockHandlerTaskRequestPo.C_HANDLER_ID, handlerId.intValue());
        this.mockHandlerTaskRequestMapper.deleteByCondition(requestExample);

        return true;
    }

}