package top.silwings.admin.repository.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.admin.common.enums.MockHandlerComponentType;
import top.silwings.admin.repository.po.ConditionPo;
import top.silwings.admin.repository.po.MockHandlerTaskPo;
import top.silwings.admin.repository.po.MockHandlerTaskRequestPo;
import top.silwings.admin.repository.po.pack.MockHandlerTaskPoWrap;
import top.silwings.admin.utils.Counter;
import top.silwings.core.common.Identity;
import top.silwings.core.model.TaskInfoDto;
import top.silwings.core.utils.ConvertUtils;

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
                .taskId(Identity.from(mockHandlerTaskPo.getTaskId()))
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
        final Counter sort = Counter.newInstance();
        return taskInfoList.stream().map(e -> this.convert2Wrap(handlerId, e, sort.increment())).collect(Collectors.toList());
    }

    private MockHandlerTaskPoWrap convert2Wrap(final Identity handlerId, final TaskInfoDto taskInfoDto, final int sort) {

        final MockHandlerTaskPoWrap taskPoWrap = new MockHandlerTaskPoWrap();
        taskPoWrap.setMockHandlerTaskPo(this.convert(handlerId, taskInfoDto, sort));
        taskPoWrap.setConditionPoList(this.conditionDaoConverter.listConvert(handlerId, MockHandlerComponentType.MOCK_HANDLER_TASK, taskInfoDto.getSupport()));
        taskPoWrap.setMockHandlerTaskRequestPo(this.mockHandlerTaskRequestDaoConverter.convert(handlerId, taskInfoDto.getRequest()));

        return taskPoWrap;
    }

    private MockHandlerTaskPo convert(final Identity handlerId, final TaskInfoDto taskInfoDto, final int sort) {
        final MockHandlerTaskPo po = new MockHandlerTaskPo();
        po.setTaskId(ConvertUtils.getNoNullOrDefault(taskInfoDto.getTaskId(), null, Identity::intValue));
        po.setHandlerId(ConvertUtils.getNoNullOrDefault(handlerId, null, Identity::intValue));
        po.setName(taskInfoDto.getName());
        po.setAsync(taskInfoDto.isAsync());
        po.setCron(taskInfoDto.getCron());
        po.setNumberOfExecute(taskInfoDto.getNumberOfExecute());
        po.setSort(sort);

        return po;
    }


}