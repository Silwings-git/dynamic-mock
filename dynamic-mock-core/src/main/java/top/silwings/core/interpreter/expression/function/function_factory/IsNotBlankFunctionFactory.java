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
 * @ClassName IsBlankFunctionFactory
 * @Description 判空函数工厂
 * @Author Silwings
 * @Date 2022/11/8 22:20
 * @Since
 **/
@Component
public class IsNotBlankFunctionFactory implements FunctionFactory {

    private static final FunctionInfo IS_NOT_BLANK_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("IsNotBlank")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .build();

    private static final String SYMBOL = "#isNotBlank(...)";

    private final IsBlankFunctionFactory isBlankFunctionFactory;

    public IsNotBlankFunctionFactory(final IsBlankFunctionFactory isBlankFunctionFactory) {
        this.isBlankFunctionFactory = isBlankFunctionFactory;
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return IS_NOT_BLANK_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "isNotBlank".equalsIgnoreCase(methodName);
    }

    @Override
    public IsNotBlankFunction buildFunction(final List<Expression> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, IS_NOT_BLANK_FUNCTION_INFO.getMinArgsNumber(), IS_NOT_BLANK_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of IsNotBlank function."));
        return IsNotBlankFunction.from(functionExpressionList, this.isBlankFunctionFactory);
    }

    /**
     * 判空函数
     * #isNotBlank(待检查字符)
     */
    public static class IsNotBlankFunction extends AbstractFunctionExpression {

        private final IsBlankFunctionFactory.IsBlankFunction isBlankFunction;

        private IsNotBlankFunction(final List<Expression> functionExpressionList, final IsBlankFunctionFactory.IsBlankFunction isBlankFunction) {
            super(functionExpressionList);
            this.isBlankFunction = isBlankFunction;
        }

        private static IsNotBlankFunction from(final List<Expression> functionExpressionList, final IsBlankFunctionFactory isBlankFunctionFactory) {
            return new IsNotBlankFunction(functionExpressionList, isBlankFunctionFactory.buildFunction(functionExpressionList));
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return !this.isBlankFunction.doInterpret(mockHandlerContext, childNodeValueList);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}