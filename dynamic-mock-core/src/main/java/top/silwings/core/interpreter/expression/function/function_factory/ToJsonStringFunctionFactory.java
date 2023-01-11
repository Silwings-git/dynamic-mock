package top.silwings.core.interpreter.expression.function.function_factory;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.Expression;
import top.silwings.core.interpreter.expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.expression.function.FunctionFactory;
import top.silwings.core.interpreter.expression.function.FunctionInfo;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.JsonUtils;

import java.util.List;

/**
 * @ClassName ToJsonStringFunctionFactory
 * @Description 对象转json字符串函数
 * @Author Silwings
 * @Date 2022/12/31 17:07
 * @Since
 **/
@Component
public class ToJsonStringFunctionFactory implements FunctionFactory {

    private static final FunctionInfo TO_JSON_STRING_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("ToJsonString")
            .minArgsNumber(1)
            .maxArgsNumber(1)
            .build();

    private static final String SYMBOL = "#toJsonString(...)";

    @Override
    public boolean support(final String methodName) {
        return "tojsonstring".equalsIgnoreCase(methodName) || "tjs".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return TO_JSON_STRING_FUNCTION_INFO;
    }

    @Override
    public ToJsonStringFunction buildFunction(final List<Expression> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, TO_JSON_STRING_FUNCTION_INFO.getMinArgsNumber(), TO_JSON_STRING_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of ToJsonString function."));
        return ToJsonStringFunction.from(functionExpressionList);
    }

    public static class ToJsonStringFunction extends AbstractFunctionExpression {

        protected ToJsonStringFunction(final List<Expression> functionExpressionList) {
            super(functionExpressionList);
        }

        public static ToJsonStringFunction from(final List<Expression> functionExpressionList) {
            return new ToJsonStringFunction(functionExpressionList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return JsonUtils.toJSONString(childNodeValueList.get(0));
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}