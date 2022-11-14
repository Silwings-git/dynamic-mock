package top.silwings.core.handler.task;

import org.springframework.stereotype.Component;
import top.silwings.core.handler.JsonNodeParser;
import top.silwings.core.handler.tree.NodeInterpreter;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.repository.dto.TaskInfoDto;
import top.silwings.core.utils.ConvertUtils;

import java.util.stream.Collectors;

/**
 * @ClassName MockResponse
 * @Description Mock响应
 * @Author Silwings
 * @Date 2022/11/10 22:17
 * @Since
 **/
@Component
public class MockTaskInfoFactory {

    private static final String DEFAULT_CRON = "* * * * * ?";

    private final DynamicValueFactory dynamicValueFactory;

    private final JsonNodeParser jsonNodeParser;

    public MockTaskInfoFactory(final DynamicValueFactory dynamicValueFactory, final JsonNodeParser jsonNodeParser) {
        this.dynamicValueFactory = dynamicValueFactory;
        this.jsonNodeParser = jsonNodeParser;
    }

    public MockTaskInfo buildTask(final TaskInfoDto definition) {
        return MockTaskInfo.builder()
                .name(definition.getName())
                .supportInterpreterList(definition.getSupport().stream().map(this.dynamicValueFactory::buildDynamicValue).map(NodeInterpreter::new).collect(Collectors.toList()))
                .async(definition.isAsync())
                .cron(ConvertUtils.getNoBlankOrDefault(definition.getCron(), DEFAULT_CRON))
                .numberOfExecute(ConvertUtils.getNoNullOrDefault(definition.getNumberOfExecute(), 1))
                .mockTaskInterpreter(new NodeInterpreter(this.jsonNodeParser.parse(definition.getRequest())))
                .build();
    }
}