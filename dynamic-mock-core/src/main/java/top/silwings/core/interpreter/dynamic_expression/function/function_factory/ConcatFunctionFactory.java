package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;

import java.util.List;

/**
 * @ClassName ConcatFunctionFactory
 * @Description 字符JOIN
 * @Author Silwings
 * @Date 2022/12/2 0:10
 * @Since
 **/
@Component
public class ConcatFunctionFactory implements FunctionFactory {

    private static final FunctionInfo CONCAT_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Concat")
            .minArgsNumber(0)
            .maxArgsNumber(Integer.MAX_VALUE)
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .build();

    private static final String SYMBOL = "#concat(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return CONCAT_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "concat".equalsIgnoreCase(methodName);
    }

    @Override
    public ConcatFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, CONCAT_FUNCTION_INFO.getMinArgsNumber(), CONCAT_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Concat function."));
        return ConcatFunctionFactory.ConcatFunction.from(functionExpressionList);
    }

    /**
     * 字符串连接函数
     * #concat(待连接字符,待连接字符...)
     */
    public static class ConcatFunction extends AbstractFunctionExpression {

        private ConcatFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        private static ConcatFunctionFactory.ConcatFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            return new ConcatFunctionFactory.ConcatFunction(functionExpressionList);
        }

        @Override
        public String doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            final StringBuilder builder = new StringBuilder();

            childNodeValueList.stream().map(e -> null == e ? null : String.valueOf(e)).forEach(builder::append);

            return builder.toString();
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}