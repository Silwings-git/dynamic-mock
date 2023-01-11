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
    public OperatorExpression buildOperator(final List<Expression> expressionList) {
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

        private MultiplicationOperator(final List<Expression> functionExpressionList) {
            super(functionExpressionList);
        }

        public static MultiplicationOperator from(final List<Expression> functionExpressionList) {
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