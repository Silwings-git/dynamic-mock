package top.silwings.core.interpreter.expression.operator.operator_factory;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.Expression;
import top.silwings.core.interpreter.expression.operator.AbstractOperatorExpression;
import top.silwings.core.interpreter.expression.operator.OperatorExpression;
import top.silwings.core.interpreter.expression.operator.OperatorFactory;
import top.silwings.core.interpreter.expression.operator.OperatorType;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName AndOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:44
 * @Since
 **/
@Component
public class AndOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = "&&";

    @Override
    public boolean support(final String symbol) {
        return "&&".equals(symbol);
    }


    @Override
    public OperatorExpression buildOperator(final List<Expression> expressionList) {
        return AndOperator.from(expressionList);
    }

    @Override
    public int getPriority() {
        return OperatorType.LOGICAL.getPriority();
    }

    @Override
    public String getOperatorSymbol() {
        return SYMBOL;
    }

    /**
     * @ClassName AndDoubleExtOperation
     * @Description 双与
     * @Author Silwings
     * @Date 2022/11/7 21:37
     * @Since
     **/
    public static class AndOperator extends AbstractOperatorExpression {

        private AndOperator(final List<Expression> functionExpressionList) {
            super(functionExpressionList);
        }

        public static AndOperator from(final List<Expression> functionExpressionList) {
            CheckUtils.hasEqualsSize(functionExpressionList, 2, DynamicValueCompileException.supplier("The operator `&&` requires 2 arguments."));
            return new AndOperator(functionExpressionList);
        }

        @Override
        public Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount() || this.getNodeCount() != 2) {
                throw new DynamicMockException("Parameter incorrectly of `&&` operator. expect: 2, actual: " + childNodeValueList.size());
            }

            return TypeUtils.toBooleanValue(childNodeValueList.get(0)) && TypeUtils.toBooleanValue(childNodeValueList.get(1));
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }
}