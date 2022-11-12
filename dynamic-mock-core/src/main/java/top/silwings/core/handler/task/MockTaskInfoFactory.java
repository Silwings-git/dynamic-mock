package top.silwings.core.handler.task;

import org.springframework.stereotype.Component;
import top.silwings.core.handler.JsonNodeParser;
import top.silwings.core.handler.tree.NodeInterpreter;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.repository.definition.ResponseInfoDefinition;
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

    public MockTaskInfo buildTask(final ResponseInfoDefinition definition) {
        return MockTaskInfo.builder()
                .name(definition.getName())
                .supportInterpreterList(definition.getSupport().stream().map(this.dynamicValueFactory::buildDynamicValue).map(NodeInterpreter::new).collect(Collectors.toList()))
                .delayTime(ConvertUtils.getNoNullOrDefault(definition.getDelayTime(), 0))
                .async(Boolean.TRUE.equals(definition.getAsync()))
                .cron(ConvertUtils.getNoBlankOrDefault(definition.getCron(), DEFAULT_CRON))
                .numberOfExecute(ConvertUtils.getNoNullOrDefault(definition.getDelayTime(), 0))
                .mockTaskInterpreter(new NodeInterpreter(this.jsonNodeParser.parse(definition.getResponseDefinition())))
                .build();
    }
}