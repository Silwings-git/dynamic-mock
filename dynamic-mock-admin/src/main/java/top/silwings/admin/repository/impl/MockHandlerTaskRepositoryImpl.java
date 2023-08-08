package top.silwings.admin.repository.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.enums.MockHandlerComponentType;
import top.silwings.admin.model.MockHandlerConditionRepository;
import top.silwings.admin.repository.MockHandlerTaskRepository;
import top.silwings.admin.repository.converter.MockHandlerTaskDaoConverter;
import top.silwings.admin.repository.mapper.MockHandlerTaskMapper;
import top.silwings.admin.repository.mapper.MockHandlerTaskRequestMapper;
import top.silwings.admin.repository.po.MockHandlerConditionPo;
import top.silwings.admin.repository.po.MockHandlerTaskPo;
import top.silwings.admin.repository.po.MockHandlerTaskRequestPo;
import top.silwings.admin.repository.po.pack.MockHandlerTaskPoWrap;
import top.silwings.core.common.Identity;
import top.silwings.core.model.TaskInfoDto;

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
            mockHandlerConditionPoList.forEach(e -> {
                e.setHandlerId(mockHandlerTaskPo.getHandlerId());
                e.setComponentId(mockHandlerTaskPo.getTaskId());
                e.setComponentType(MockHandlerComponentType.MOCK_HANDLER_TASK);
                this.mockHandlerConditionRepository.insertSelective(e);
            });

            final MockHandlerTaskRequestPo mockHandlerTaskRequestPo = mockHandlerTaskPoWrap.getMockHandlerTaskRequestPo();
            mockHandlerTaskRequestPo.setHandlerId(mockHandlerTaskPo.getHandlerId());
            mockHandlerTaskRequestPo.setTaskId(mockHandlerTaskPo.getTaskId());
            this.mockHandlerTaskRequestMapper.insertSelective(mockHandlerTaskRequestPo);
        });
    }

    @Override
    @Transactional
    public boolean removeMockHandlerTask(final Identity handlerId) {
        final Example taskExample = new Example(MockHandlerTaskPo.class);
        taskExample.createCriteria()
                .andEqualTo(MockHandlerTaskPo.C_HANDLER_ID, handlerId.intValue());
        this.mockHandlerTaskMapper.deleteByCondition(taskExample);

        this.mockHandlerConditionRepository.deleteByHandlerId(handlerId, MockHandlerComponentType.MOCK_HANDLER_TASK);

        final Example requestExample = new Example(MockHandlerTaskRequestPo.class);
        taskExample.createCriteria()
                .andEqualTo(MockHandlerTaskRequestPo.C_HANDLER_ID, handlerId.intValue());
        this.mockHandlerTaskRequestMapper.deleteByCondition(requestExample);

        return true;
    }

}