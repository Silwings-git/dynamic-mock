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

import java.util.List;

/**
 * @ClassName GreaterOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:51
 * @Since
 **/
@Component
public class GreaterOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = ">";

    @Override
    public boolean support(final String symbol) {
        return ">".equals(symbol);
    }

    @Override
    public OperatorExpression buildOperator(final List<ExpressionTreeNode> expressionList) {
        return GreaterOperator.from(expressionList);
    }

    @Override
    public int getPriority() {
        return OperatorType.COMPARISON.getPriority();
    }

    @Override
    public String getOperatorSymbol() {
        return SYMBOL;
    }

    /**
     * @ClassName GreaterExtOperation
     * @Description 大于
     * @Author Silwings
     * @Date 2022/11/7 21:33
     * @Since
     **/
    public static class GreaterOperator extends AbstractOperatorExpression {

        private GreaterOperator(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static GreaterOperator from(final List<ExpressionTreeNode> functionExpressionList) {
            CheckUtils.hasEqualsSize(functionExpressionList, 2, DynamicValueCompileException.supplier("The operator `>` requires 2 arguments."));
            return new GreaterOperator(functionExpressionList);
        }

        @Override
        public Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount() || this.getNodeCount() != 2) {
                throw new DynamicMockException("Parameter incorrectly of `>` operator. expect: 2, actual: " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).compareTo(TypeUtils.toBigDecimal(childNodeValueList.get(1))) > 0;
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }

    }
}