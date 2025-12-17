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
 * @ClassName LessEqualOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:52
 * @Since
 **/
@Component
public class LessEqualOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = "<=";

    @Override
    public boolean support(final String symbol) {
        return "<=".equals(symbol);
    }

    @Override
    public OperatorExpression buildOperator(final List<ExpressionTreeNode> expressionList) {
        return LessEqualOperator.from(expressionList);
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
     * @ClassName LessEqualExtOperation
     * @Description 小于等于
     * @Author Silwings
     * @Date 2022/11/7 21:35
     * @Since
     **/
    public static class LessEqualOperator extends AbstractOperatorExpression {

        private LessEqualOperator(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        public static LessEqualOperator from(final List<ExpressionTreeNode> functionExpressionList) {
            CheckUtils.hasEqualsSize(functionExpressionList, 2, DynamicValueCompileException.supplier("The operator `<=` requires 2 arguments."));
            return new LessEqualOperator(functionExpressionList);
        }

        @Override
        public Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount() || this.getNodeCount() != 2) {
                throw new DynamicMockException("Parameter incorrectly of `<=` operator. expect: 2, actual: " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).compareTo(TypeUtils.toBigDecimal(childNodeValueList.get(1))) <= 0;
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }
}