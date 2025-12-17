package cn.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import cn.silwings.core.exceptions.DynamicMockException;
import cn.silwings.core.exceptions.DynamicValueCompileException;
import cn.silwings.core.handler.context.MockHandlerContext;
import cn.silwings.core.interpreter.ExpressionTreeNode;
import cn.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionExpression;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import cn.silwings.core.utils.CheckUtils;
import cn.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName StartWithFunctionFactory
 * @Description 判断是否以指定字符开头
 * @Author Silwings
 * @Date 2023/7/27 22:10
 * @Since
 **/
@Component
public class StartsWithFunctionFactory implements FunctionFactory {

    private static final FunctionInfo STARTS_WITH_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("StartsWith")
            .minArgsNumber(2)
            .maxArgsNumber(3)
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .description("判断字符串是否以指定前缀开头。接收2~3个参数：待检查字符串、前缀、是否忽略大小写（可选，默认false）。")
            .example("#StartsWith(#Search($.username), 'admin')\n" +
                    "#StartsWith(#Search($.email), 'test', true)")
            .build();

    private static final String SYMBOL = "#StartsWith(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return STARTS_WITH_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "startswith".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionExpression buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        return StartWithFunction.from(functionExpressionList);
    }

    /**
     * 判断指定字符是否以prefix开头
     * #StartsWith(待检查字符,prefix)
     * #StartsWith(待检查字符,prefix,ignoreCase)
     */
    public static class StartWithFunction extends AbstractFunctionExpression {

        private StartWithFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        private static StartWithFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            CheckUtils.sizeBetween(functionExpressionList, STARTS_WITH_FUNCTION_INFO.getMinArgsNumber(), STARTS_WITH_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of StartsWith function."));
            return new StartWithFunction(functionExpressionList);
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (childNodeValueList.size() != this.getNodeCount()) {
                throw new DynamicMockException("Parameter incorrectly of `StartsWith` function. expect: " + STARTS_WITH_FUNCTION_INFO.getMinArgsNumber() + ", actual: " + childNodeValueList.size());
            }

            final String first = String.valueOf(childNodeValueList.get(0));
            final String prefix = String.valueOf(childNodeValueList.get(1));
            final boolean ignoreCase;
            if (childNodeValueList.size() > 2) {
                ignoreCase = TypeUtils.toBooleanValue(childNodeValueList.get(2));
            } else {
                ignoreCase = false;
            }

            return ignoreCase ? StringUtils.startsWithIgnoreCase(first, prefix) : StringUtils.startsWith(first, prefix);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}