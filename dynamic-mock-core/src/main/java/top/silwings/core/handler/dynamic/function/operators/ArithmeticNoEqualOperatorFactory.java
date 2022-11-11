package top.silwings.core.handler.dynamic.function.operators;

import org.springframework.stereotype.Component;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.AbstractDynamicValue;
import top.silwings.core.handler.dynamic.function.OperatorFactory;
import top.silwings.core.handler.dynamic.function.OperatorType;

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
    public DynamicValue buildFunction(final DynamicValue param) {
        return new ArithmeticNoEqualOperator(param, this.arithmeticEqualOperatorFactory);
    }

    /**
     * @ClassName ArithmeticNoEqualExtOperation
     * @Description 数值不等
     * @Author Silwings
     * @Date 2022/11/7 21:30
     * @Since
     **/
    public static class ArithmeticNoEqualOperator extends AbstractDynamicValue implements DynamicValue {

        private final ArithmeticEqualOperatorFactory.ArithmeticEqualOperator arithmeticEqualOperator;

        public ArithmeticNoEqualOperator(final DynamicValue param, final ArithmeticEqualOperatorFactory arithmeticEqualOperatorFactory) {
            super(param);
            this.arithmeticEqualOperator = (ArithmeticEqualOperatorFactory.ArithmeticEqualOperator) arithmeticEqualOperatorFactory.buildFunction(param);
        }

        @Override
        public Boolean interpret(final Context context) {
            return !this.arithmeticEqualOperator.interpret(context);
        }

    }

    @Override
    public int getPriority() {
        return OperatorType.COMPARISON.getPriority();
    }

    @Override
    public String getOperatorSymbol() {
        return SYMBOL;
    }
}