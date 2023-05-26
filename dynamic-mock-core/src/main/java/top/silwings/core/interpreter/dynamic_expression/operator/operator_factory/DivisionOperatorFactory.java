package top.silwings.core.interpreter.dynamic_expression.operator.operator_factory;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.operator.AbstractOperatorExpression;
import top.silwings.core.interpreter.dynamic_expression.operator.OperatorExpression;
import top.silwings.core.interpreter.dynamic_expression.operator.OperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.OperatorType;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.TypeUtils;

import java.math.MathContext;
import java.util.List;

/**
 * @ClassName DivisionOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:49
 * @Since
 **/
@Component
public class DivisionOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = "/";

    @Override
    public boolean support(final String methodName) {
        return "/".equals(methodName);
    }

    @Override
    public OperatorExpression buildOperator(final List<ExpressionTreeNode> expressionList) {
        return DivisionOperator.from(expressionList);
    }

    @Override
    public int getPriority() {
        return OperatorType.ARITHMETIC_TWO.getPriority();
    }

    @Override
    public String getOperatorSymbol() {
        return SYMBOL;
    }

    /**
     * @ClassName DivisionExtOperation
     * @Description 除法
     * @Author Silwings
     * @Date 2022/11/7 21:27
     * @Since
     **/
    public static class DivisionOperator extends AbstractOperatorExpression {

        private DivisionOperator(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static DivisionOperator from(final List<ExpressionTreeNode> functionExpressionList) {
            CheckUtils.hasEqualsSize(functionExpressionList, 2, DynamicValueCompileException.supplier("The operator `/` requires 2 arguments."));
            return new DivisionOperator(functionExpressionList);
        }

        @Override
        public Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount() || this.getNodeCount() != 2) {
                throw new DynamicMockException("Parameter incorrectly of `/` operator. expect: 2, actual: " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).divide(TypeUtils.toBigDecimal(childNodeValueList.get(1)), MathContext.DECIMAL64);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }

    }
}