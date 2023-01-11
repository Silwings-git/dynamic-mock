package top.silwings.core.interpreter.dynamic_expression.operator.operator_factory;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.operator.AbstractOperatorExpression;
import top.silwings.core.interpreter.dynamic_expression.operator.OperatorExpression;
import top.silwings.core.interpreter.dynamic_expression.operator.OperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.OperatorType;
import top.silwings.core.utils.CheckUtils;

import java.util.List;

/**
 * @ClassName ArithmeticNoEqualOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:48
 * @Since
 **/
@Component
public class ArithmeticNoEqualOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = "!=";

    private final ArithmeticEqualOperatorFactory arithmeticEqualOperatorFactory;

    public ArithmeticNoEqualOperatorFactory(final ArithmeticEqualOperatorFactory arithmeticEqualOperatorFactory) {
        this.arithmeticEqualOperatorFactory = arithmeticEqualOperatorFactory;
    }

    @Override
    public boolean support(final String symbol) {
        return "!=".equals(symbol);
    }

    @Override
    public OperatorExpression buildOperator(final List<ExpressionTreeNode> expressionList) {
        return ArithmeticNoEqualOperator.of(expressionList, this.arithmeticEqualOperatorFactory);
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
     * @ClassName ArithmeticNoEqualExtOperation
     * @Description 数值不等
     * @Author Silwings
     * @Date 2022/11/7 21:30
     * @Since
     **/
    public static class ArithmeticNoEqualOperator extends AbstractOperatorExpression {

        private final ArithmeticEqualOperatorFactory.ArithmeticEqualOperator arithmeticEqualOperator;

        private ArithmeticNoEqualOperator(final List<ExpressionTreeNode> functionExpressionList, final ArithmeticEqualOperatorFactory arithmeticEqualOperatorFactory) {
            super(functionExpressionList);
            this.arithmeticEqualOperator = (ArithmeticEqualOperatorFactory.ArithmeticEqualOperator) arithmeticEqualOperatorFactory.buildOperator(functionExpressionList);
        }

        public static ArithmeticNoEqualOperator of(final List<ExpressionTreeNode> functionExpressionList, final ArithmeticEqualOperatorFactory arithmeticEqualOperatorFactory) {
            CheckUtils.hasEqualsSize(functionExpressionList, 2, DynamicValueCompileException.supplier("The operator `!=` requires 2 arguments."));
            return new ArithmeticNoEqualOperator(functionExpressionList, arithmeticEqualOperatorFactory);
        }

        @Override
        public Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return Boolean.FALSE.equals(this.arithmeticEqualOperator.interpret(mockHandlerContext, childNodeValueList));
        }

        @Override
        public int getNodeCount() {
            return this.arithmeticEqualOperator.getNodeCount();
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }
}