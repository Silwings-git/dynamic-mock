package top.silwings.core.handler.dynamic.operator.operators;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.operator.OperatorFactory;
import top.silwings.core.handler.dynamic.operator.OperatorType;
import top.silwings.core.utils.TypeUtils;

import java.math.BigDecimal;
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
    public DynamicValue buildFunction(final DynamicValue param) {
        return new MultiplicationOperator(param);
    }

    /**
     * @ClassName MultiplicationExtOperation
     * @Description 乘法
     * @Author Silwings
     * @Date 2022/11/7 21:26
     * @Since
     **/
    public static class MultiplicationOperator extends AbstractDynamicValue implements DynamicValue {

        public MultiplicationOperator(final DynamicValue param) {
            super(param);
        }

        @Override
        public BigDecimal interpret(final Context parameterContext) {
            final List<Object> paramList = this.getParams(parameterContext);
            if (paramList.size() < 2) {
                throw new DynamicDataException("参数长度错误,需要 2,实际 " + paramList.size());
            }

            return TypeUtils.toBigDecimal(paramList.get(0)).multiply(TypeUtils.toBigDecimal(paramList.get(1)));
        }

    }

    @Override
    public int getPriority() {
        return OperatorType.ARITHMETIC_TWO.getPriority();
    }

    @Override
    public String getOperatorSymbol() {
        return SYMBOL;
    }
}