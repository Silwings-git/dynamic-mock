package top.silwings.core.interpreter.expression.function.function_factory;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.Expression;
import top.silwings.core.interpreter.expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.expression.function.FunctionFactory;
import top.silwings.core.interpreter.expression.function.FunctionInfo;
import top.silwings.core.interpreter.expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;

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
    public EqualsFunction buildFunction(final List<Expression> functionExpressionList) {
        return EqualsFunction.from(functionExpressionList);
    }

    /**
     * 相等函数
     * #equal(待检查字符A,待检查字符B)
     * #eq(待检查字符A,待检查字符B)
     */
    public static class EqualsFunction extends AbstractFunctionExpression {

        private EqualsFunction(final List<Expression> functionExpressionList) {
            super(functionExpressionList);
        }

        private static EqualsFunction from(final List<Expression> functionExpressionList) {
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