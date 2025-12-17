package cn.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.springframework.stereotype.Component;
import cn.silwings.core.exceptions.DynamicMockException;
import cn.silwings.core.exceptions.DynamicValueCompileException;
import cn.silwings.core.handler.context.MockHandlerContext;
import cn.silwings.core.interpreter.ExpressionTreeNode;
import cn.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import cn.silwings.core.utils.CheckUtils;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName EqualsFunctionFactory
 * @Description 相等函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class EqualsFunctionFactory implements FunctionFactory {

    private static final FunctionInfo EQUALS_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Equals")
            .minArgsNumber(2)
            .maxArgsNumber(2)
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .description("比较两个字符串是否相等。支持别名 eq。接收2个参数，返回布尔值。")
            .example("#Equals(#Search($.userId), '123')\n" +
                    "#eq(#Search($.status), 'active')")
            .build();

    private static final String SYMBOL = "#equals(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return EQUALS_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "eq".equalsIgnoreCase(methodName) || "equals".equalsIgnoreCase(methodName);
    }

    @Override
    public EqualsFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        return EqualsFunction.from(functionExpressionList);
    }

    /**
     * 相等函数
     * #equal(待检查字符A,待检查字符B)
     * #eq(待检查字符A,待检查字符B)
     */
    public static class EqualsFunction extends AbstractFunctionExpression {

        private EqualsFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        private static EqualsFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            CheckUtils.sizeBetween(functionExpressionList, EQUALS_FUNCTION_INFO.getMinArgsNumber(), EQUALS_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Equals function."));
            return new EqualsFunction(functionExpressionList);
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (childNodeValueList.size() != this.getNodeCount()) {
                throw new DynamicMockException("Parameter incorrectly of `equals` function. expect: " + EQUALS_FUNCTION_INFO.getMinArgsNumber() + ", actual: " + childNodeValueList.size());
            }

            final String first = String.valueOf(childNodeValueList.get(0));
            final String second = String.valueOf(childNodeValueList.get(1));

            return Objects.equals(first, second);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}