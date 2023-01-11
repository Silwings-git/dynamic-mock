package top.silwings.core.interpreter.expression.function.function_factory;

import lombok.extern.slf4j.Slf4j;
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
 * @ClassName PrintFunctionFactory
 * @Description 打印函数
 * @Author Silwings
 * @Date 2022/12/31 17:01
 * @Since
 **/
@Slf4j
@Component
public class PrintFunctionFactory implements FunctionFactory {

    private static final FunctionInfo PRINT_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Print")
            .minArgsNumber(1)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.OBJECT)
            .build();

    private static final String SYMBOL = "#print(...)";

    @Override
    public boolean support(final String methodName) {
        return "print".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return PRINT_FUNCTION_INFO;
    }

    @Override
    public PrintFunction buildFunction(final List<Expression> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, PRINT_FUNCTION_INFO.getMinArgsNumber(), PRINT_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Print function."));
        return PrintFunction.from(functionExpressionList);
    }

    public static class PrintFunction extends AbstractFunctionExpression {

        protected PrintFunction(final List<Expression> functionExpressionList) {
            super(functionExpressionList);
        }

        public static PrintFunction from(final List<Expression> functionExpressionList) {
            return new PrintFunction(functionExpressionList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            log.info("Print Function :{}", childNodeValueList.get(0));

            return childNodeValueList.get(0);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}