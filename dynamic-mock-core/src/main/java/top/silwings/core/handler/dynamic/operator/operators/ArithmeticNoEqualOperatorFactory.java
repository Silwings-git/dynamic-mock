package top.silwings.core.handler.dynamic.operator.operators;

import org.springframework.stereotype.Component;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.operator.OperatorFactory;
import top.silwings.core.handler.dynamic.operator.OperatorType;

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
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return new ArithmeticNoEqualOperator(dynamicValueList, this.arithmeticEqualOperatorFactory);
    }

    /**
     * @ClassName ArithmeticNoEqualExtOperation
     * @Description 数值不等
     * @Author Silwings
     * @Date 2022/11/7 21:30
     * @Since
     **/
    public static class ArithmeticNoEqualOperator extends AbstractDynamicValue {

        private final ArithmeticEqualOperatorFactory.ArithmeticEqualOperator arithmeticEqualOperator;

        public ArithmeticNoEqualOperator(final List<DynamicValue> dynamicValueList, final ArithmeticEqualOperatorFactory arithmeticEqualOperatorFactory) {
            super(dynamicValueList);
            this.arithmeticEqualOperator = (ArithmeticEqualOperatorFactory.ArithmeticEqualOperator) arithmeticEqualOperatorFactory.buildFunction(dynamicValueList);
        }

        @Override
        public Object interpret(final Context context, final List<Object> childNodeValueList) {
            return Boolean.FALSE.equals(this.arithmeticEqualOperator.interpret(context, childNodeValueList));
        }

        @Override
        public int getNodeCount() {
            return this.arithmeticEqualOperator.getNodeCount();
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