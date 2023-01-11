package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;

import java.util.List;

/**
 * @ClassName IsBlankFunctionFactory
 * @Description 判空函数工厂
 * @Author Silwings
 * @Date 2022/12/14 23:46
 * @Since
 **/
@Component
public class IsNullFunctionFactory implements FunctionFactory {

    private static final FunctionInfo IS_NULL_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("IsNull")
            .minArgsNumber(0)
            .maxArgsNumber(1)
            .functionReturnType(FunctionReturnType.BOOLEAN)
            .build();

    private static final String SYMBOL = "#IsNull(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return IS_NULL_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "isNull".equalsIgnoreCase(methodName);
    }

    @Override
    public IsNullFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, IS_NULL_FUNCTION_INFO.getMinArgsNumber(), IS_NULL_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of IsNull function."));
        return IsNullFunction.from(functionExpressionList);
    }

    /**
     * 判空函数
     * #isNull(待检查实例)
     */
    public static class IsNullFunction extends AbstractFunctionExpression {

        private IsNullFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        private static IsNullFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            return new IsNullFunction(functionExpressionList);
        }

        @Override
        public Boolean doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return CollectionUtils.isEmpty(childNodeValueList) || null == childNodeValueList.get(0);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}