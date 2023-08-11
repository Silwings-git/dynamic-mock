package top.silwings.core.handler.response;

import org.springframework.stereotype.Component;
import top.silwings.core.handler.check.CheckInfoFactory;
import top.silwings.core.interpreter.ExpressionInterpreter;
import top.silwings.core.interpreter.dynamic_expression.DynamicExpressionFactory;
import top.silwings.core.interpreter.json.JsonTreeParser;
import top.silwings.core.model.MockResponseInfoDto;
import top.silwings.core.utils.ConvertUtils;

import java.util.stream.Collectors;

/**
 * @ClassName MockResponseInfoFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 20:55
 * @Since
 **/
@Component
public class MockResponseInfoFactory {

    private final DynamicExpressionFactory dynamicExpressionFactory;

    private final JsonTreeParser jsonTreeParser;

    private final CheckInfoFactory checkInfoFactory;

    public MockResponseInfoFactory(final DynamicExpressionFactory dynamicExpressionFactory, final JsonTreeParser jsonTreeParser, final CheckInfoFactory checkInfoFactory) {
        this.dynamicExpressionFactory = dynamicExpressionFactory;
        this.jsonTreeParser = jsonTreeParser;
        this.checkInfoFactory = checkInfoFactory;
    }

    public MockResponseInfo buildResponseInfo(final MockResponseInfoDto definition) {
        return MockResponseInfo.builder()
                .name(definition.getName())
                .supportInterpreterList(definition.getSupport().stream().map(this.dynamicExpressionFactory::buildDynamicValue).map(ExpressionInterpreter::new).collect(Collectors.toList()))
                .delayTime(ConvertUtils.getNoNullOrDefault(definition.getDelayTime(), 0))
                .checkInfo(this.checkInfoFactory.buildCheck(definition.getCheckInfo()))
                .responseInterpreter(new ExpressionInterpreter(this.jsonTreeParser.parse(definition.getResponse())))
                .build();
    }

}