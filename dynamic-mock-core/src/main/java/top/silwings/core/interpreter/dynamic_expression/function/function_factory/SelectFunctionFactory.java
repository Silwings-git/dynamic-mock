package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.springframework.stereotype.Component;
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
 * @ClassName SelectFunctionFactory
 * @Description 选择函数(三元运算符)
 * @Author Silwings
 * @Date 2023/1/8 22:21
 * @Since
 **/
@Component
public class SelectFunctionFactory implements FunctionFactory {

    private static final FunctionInfo SELECT_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Concat")
            .minArgsNumber(3)
            .maxArgsNumber(3)
            .functionReturnType(FunctionReturnType.OBJECT)
            .build();

    private static final String SYMBOL = "#select(...)";

    @Override
    public boolean support(final String methodName) {
        return "select".equalsIgnoreCase(methodName);
    }

    @Override
    public FunctionInfo getFunctionInfo() {
        return SELECT_FUNCTION_INFO;
    }

    @Override
    public FunctionExpression buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, SELECT_FUNCTION_INFO.getMinArgsNumber(), SELECT_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Select function."));
        return SelectFunction.from(functionExpressionList);
    }

    /**
     * 选择函数
     * 等于三元运算表达式
     * #select(boolean,argA,argB)
     */
    public static class SelectFunction extends AbstractFunctionExpression {

        public SelectFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static SelectFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            return new SelectFunction(functionExpressionList);
        }

        @Override
        protected Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return Boolean.TRUE.equals(TypeUtils.toBooleanValue(childNodeValueList.get(0))) ? childNodeValueList.get(1) : childNodeValueList.get(2);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }

    }

}