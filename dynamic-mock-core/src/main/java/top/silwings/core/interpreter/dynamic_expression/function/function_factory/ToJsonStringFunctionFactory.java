package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
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
@Slf4j
@Component
public class ToJsonStringFunctionFactory implements FunctionFactory {

    private static final FunctionInfo TO_JSON_STRING_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("ToJsonString")
            .minArgsNumber(1)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.STRING)
            .description("对象转JSON字符串函数。将Java对象序列化为JSON格式字符串。支持别名：ToJsonStr, TJS。")
            .example("#ToJsonString(#Search($.data))\n" +
                    "#TJS(#Search($.userInfo))")
            .build();

    private static final String SYMBOL = "#toJsonString(...)";

    @Override
    public boolean support(final String methodName) {
        return "tojsonstring".equalsIgnoreCase(methodName) || "tojsonstr".equalsIgnoreCase(methodName) || "tjs".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return TO_JSON_STRING_FUNCTION_INFO;
    }

    @Override
    public ToJsonStringFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, TO_JSON_STRING_FUNCTION_INFO.getMinArgsNumber(), TO_JSON_STRING_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of ToJsonString function."));
        return ToJsonStringFunction.from(functionExpressionList);
    }

    /**
     * #toJsonString(arg)
     * #toJsonString(arg,strict)
     */
    public static class ToJsonStringFunction extends AbstractFunctionExpression {

        protected ToJsonStringFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static ToJsonStringFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            return new ToJsonStringFunction(functionExpressionList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            try {
                return JsonUtils.toJSONString(childNodeValueList.get(0));
            } catch (Exception e) {
                return childNodeValueList.get(0);
            }
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}