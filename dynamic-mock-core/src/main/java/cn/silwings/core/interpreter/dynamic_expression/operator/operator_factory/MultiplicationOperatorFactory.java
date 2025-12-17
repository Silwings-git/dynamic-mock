package cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory;

import org.springframework.stereotype.Component;
import cn.silwings.core.exceptions.DynamicMockException;
import cn.silwings.core.exceptions.DynamicValueCompileException;
import cn.silwings.core.handler.context.MockHandlerContext;
import cn.silwings.core.interpreter.ExpressionTreeNode;
import cn.silwings.core.interpreter.dynamic_expression.operator.AbstractOperatorExpression;
import cn.silwings.core.interpreter.dynamic_expression.operator.OperatorExpression;
import cn.silwings.core.interpreter.dynamic_expression.operator.OperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.OperatorType;
import cn.silwings.core.utils.CheckUtils;
import cn.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName MultiplicationOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:55
 * @Since
 **/
@Component
public class MultiplicationOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = "*";

    @Override
    public boolean support(final String symbol) {
        return "*".equals(symbol);
    }

    @Override
    public OperatorExpression buildOperator(final List<ExpressionTreeNode> expressionList) {
        return MultiplicationOperator.from(expressionList);
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
     * @ClassName MultiplicationExtOperation
     * @Description 乘法
     * @Author Silwings
     * @Date 2022/11/7 21:26
     * @Since
     **/
    public static class MultiplicationOperator extends AbstractOperatorExpression {

        private MultiplicationOperator(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static MultiplicationOperator from(final List<ExpressionTreeNode> functionExpressionList) {
            CheckUtils.hasEqualsSize(functionExpressionList, 2, DynamicValueCompileException.supplier("The operator `*` requires 2 arguments."));
            return new MultiplicationOperator(functionExpressionList);
        }

        @Override
        public Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount() || this.getNodeCount() != 2) {
                throw new DynamicMockException("Parameter incorrectly of `*` operator. expect: 2, actual: " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).multiply(TypeUtils.toBigDecimal(childNodeValueList.get(1)));
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }
}