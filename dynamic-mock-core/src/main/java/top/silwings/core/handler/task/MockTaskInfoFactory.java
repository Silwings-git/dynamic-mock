package top.silwings.core.handler.task;

import org.springframework.stereotype.Component;
import top.silwings.core.common.Identity;
import top.silwings.core.interpreter.ExpressionInterpreter;
import top.silwings.core.interpreter.expression.DynamicExpressionFactory;
import top.silwings.core.interpreter.json.JsonTreeParser;
import top.silwings.core.model.TaskInfoDto;
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

    private final DynamicExpressionFactory dynamicExpressionFactory;

    private final JsonTreeParser jsonTreeParser;

    public MockTaskInfoFactory(final DynamicExpressionFactory dynamicExpressionFactory, final JsonTreeParser jsonTreeParser) {
        this.dynamicExpressionFactory = dynamicExpressionFactory;
        this.jsonTreeParser = jsonTreeParser;
    }

    public MockTaskInfo buildTask(final Identity handlerId, final TaskInfoDto definition) {
        return MockTaskInfo.builder()
                .handlerId(handlerId)
                .name(definition.getName())
                .support(definition.getSupport())
                .supportInterpreterList(definition.getSupport().stream().map(this.dynamicExpressionFactory::buildDynamicValue).map(ExpressionInterpreter::new).collect(Collectors.toList()))
                .async(definition.isAsync())
                .cron(ConvertUtils.getNoBlankOrDefault(definition.getCron(), DEFAULT_CRON))
                .numberOfExecute(ConvertUtils.getNoNullOrDefault(definition.getNumberOfExecute(), 1))
                .mockTaskInterpreter(new ExpressionInterpreter(this.jsonTreeParser.parse(definition.getRequest())))
                .build();
    }

}