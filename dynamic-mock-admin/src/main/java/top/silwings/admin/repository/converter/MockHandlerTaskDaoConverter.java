package top.silwings.admin.repository.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.admin.common.enums.MockHandlerComponentType;
import top.silwings.admin.repository.po.ConditionPo;
import top.silwings.admin.repository.po.MockHandlerTaskPo;
import top.silwings.admin.repository.po.MockHandlerTaskRequestPo;
import top.silwings.admin.repository.po.pack.MockHandlerTaskPoWrap;
import top.silwings.core.common.Identity;
import top.silwings.core.model.TaskInfoDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerTaskDaoConverter
 * @Description
 * @Author Silwings
 * @Date 2023/8/7 14:50
 * @Since
 **/
@Component
public class MockHandlerTaskDaoConverter {

    private final MockHandlerTaskRequestDaoConverter mockHandlerTaskRequestDaoConverter;

    private final ConditionDaoConverter conditionDaoConverter;

    public MockHandlerTaskDaoConverter(final MockHandlerTaskRequestDaoConverter mockHandlerTaskRequestDaoConverter, final ConditionDaoConverter conditionDaoConverter) {
        this.mockHandlerTaskRequestDaoConverter = mockHandlerTaskRequestDaoConverter;
        this.conditionDaoConverter = conditionDaoConverter;
    }

    public TaskInfoDto convert(final MockHandlerTaskPo mockHandlerTaskPo, final List<ConditionPo> conditionPoList, final MockHandlerTaskRequestPo taskRequestPo) {
        return TaskInfoDto.builder()
                .name(mockHandlerTaskPo.getName())
                .support(conditionPoList.stream().map(ConditionPo::getExpression).collect(Collectors.toList()))
                .async(mockHandlerTaskPo.getAsync())
                .cron(mockHandlerTaskPo.getCron())
                .numberOfExecute(mockHandlerTaskPo.getNumberOfExecute())
                .request(this.mockHandlerTaskRequestDaoConverter.convert(taskRequestPo))
                .build();
    }

    public List<MockHandlerTaskPoWrap> listConvert(final Identity handlerId, final List<TaskInfoDto> taskInfoList) {

        if (CollectionUtils.isEmpty(taskInfoList)) {
            return Collections.emptyList();
        }

        return taskInfoList.stream().map(e -> this.convert2Wrap(handlerId, e)).collect(Collectors.toList());
    }

    private MockHandlerTaskPoWrap convert2Wrap(final Identity handlerId, final TaskInfoDto taskInfoDto) {

        final MockHandlerTaskPoWrap taskPoWrap = new MockHandlerTaskPoWrap();
        taskPoWrap.setMockHandlerTaskPo(this.convert(handlerId, taskInfoDto));
        taskPoWrap.setConditionPoList(this.conditionDaoConverter.listConvert(handlerId, MockHandlerComponentType.MOCK_HANDLER_TASK, taskInfoDto.getSupport()));
        taskPoWrap.setMockHandlerTaskRequestPo(this.mockHandlerTaskRequestDaoConverter.convert(handlerId, taskInfoDto.getRequest()));

        return taskPoWrap;
    }

    private MockHandlerTaskPo convert(final Identity handlerId, final TaskInfoDto taskInfoDto) {
        final MockHandlerTaskPo po = new MockHandlerTaskPo();
        po.setTaskId(null);
        po.setHandlerId(handlerId.intValue());
        po.setName(taskInfoDto.getName());
        po.setAsync(taskInfoDto.isAsync());
        po.setCron(taskInfoDto.getCron());
        po.setNumberOfExecute(taskInfoDto.getNumberOfExecute());

        return po;
    }


}