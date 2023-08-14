package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName EndsWithFunctionFactory
 * @Description 判断是否以指定字符结尾
 * @Author Silwings
 * @Date 2023/7/27 22:26
 * @Since
 **/
@Component
public class EndsWithFunctionFactory implements FunctionFactory {

    private static final FunctionInfo ENDS_WITH_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("EndsWith")
            .minArgsNumber(2)
            .maxArgsNumber(3)
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .build();

    private static final String SYMBOL = "#EndsWith(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return ENDS_WITH_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "EndsWith".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionExpression buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        return StartWithFunction.from(functionExpressionList);
    }

    /**
     * 判断指定字符是否以suffix结尾
     * #EndsWith(待检查字符,suffix)
     * #EndsWith(待检查字符,suffix,ignoreCase)
     */
    public static class StartWithFunction extends AbstractFunctionExpression {

        private StartWithFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        private static StartWithFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            CheckUtils.sizeBetween(functionExpressionList, ENDS_WITH_FUNCTION_INFO.getMinArgsNumber(), ENDS_WITH_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of EndsWith function."));
            return new StartWithFunction(functionExpressionList);
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (childNodeValueList.size() != this.getNodeCount()) {
                throw new DynamicMockException("Parameter incorrectly of `EndsWith` function. expect: " + ENDS_WITH_FUNCTION_INFO.getMinArgsNumber() + ", actual: " + childNodeValueList.size());
            }

            final String first = String.valueOf(childNodeValueList.get(0));
            final String suffix = String.valueOf(childNodeValueList.get(1));
            final boolean ignoreCase;
            if (childNodeValueList.size() > 2) {
                ignoreCase = TypeUtils.toBooleanValue(childNodeValueList.get(2));
            } else {
                ignoreCase = false;
            }

            return ignoreCase ? StringUtils.endsWithIgnoreCase(first, suffix) : StringUtils.endsWith(first, suffix);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}