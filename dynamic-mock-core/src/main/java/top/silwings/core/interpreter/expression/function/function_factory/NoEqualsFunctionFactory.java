package top.silwings.core.interpreter.expression.function.function_factory;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.Expression;
import top.silwings.core.interpreter.expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.expression.function.FunctionFactory;
import top.silwings.core.interpreter.expression.function.FunctionInfo;
import top.silwings.core.interpreter.expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;

import java.util.List;

/**
 * @ClassName EqualsFunctionFactory
 * @Description 不等函数工厂
 * @Author Silwings
 * @Date 2022/12/17 18:24
 * @Since
 **/
@Component
public class NoEqualsFunctionFactory implements FunctionFactory {

    private static final FunctionInfo NO_EQUALS_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("NoEquals")
            .minArgsNumber(2)
            .maxArgsNumber(2)
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .build();

    private static final String SYMBOL = "#noEquals(...)";

    private final EqualsFunctionFactory equalsFunctionFactory;

    public NoEqualsFunctionFactory(final EqualsFunctionFactory equalsFunctionFactory) {
        this.equalsFunctionFactory = equalsFunctionFactory;
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return NO_EQUALS_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "neq".equalsIgnoreCase(methodName) || "noEquals".equalsIgnoreCase(methodName);
    }

    @Override
    public NoEqualsFunction buildFunction(final List<Expression> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, NO_EQUALS_FUNCTION_INFO.getMinArgsNumber(), NO_EQUALS_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of NoEquals function."));
        return NoEqualsFunction.from(functionExpressionList, this.equalsFunctionFactory.buildFunction(functionExpressionList));
    }

    /**
     * 相等函数
     * #noEqual(待检查字符A,待检查字符B)
     * #neq(待检查字符A,待检查字符B)
     */
    public static class NoEqualsFunction extends AbstractFunctionExpression {

        private final EqualsFunctionFactory.EqualsFunction equalsFunction;

        private NoEqualsFunction(final List<Expression> functionExpressionList, final EqualsFunctionFactory.EqualsFunction equalsFunction) {
            super(functionExpressionList);
            this.equalsFunction = equalsFunction;
        }

        public static NoEqualsFunction from(final List<Expression> functionExpressionList, final EqualsFunctionFactory.EqualsFunction equalsFunction) {
            return new NoEqualsFunction(functionExpressionList, equalsFunction);
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return !this.equalsFunction.doInterpret(mockHandlerContext, childNodeValueList);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}