package top.silwings.core.handler.dynamic.function.operators;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.AbstractDynamicValue;
import top.silwings.core.handler.dynamic.function.OperatorFactory;
import top.silwings.core.handler.dynamic.function.OperatorType;
import top.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName GreaterEqualOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:50
 * @Since
 **/
@Component
public class GreaterEqualOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = ">=";

    @Override
    public boolean support(final String symbol) {
        return ">=".equals(symbol);
    }

    @Override
    public DynamicValue buildFunction(final DynamicValue param) {
        return new GreaterEqualOperator(param);
    }

    /**
     * @ClassName GreaterEqualExtOperation
     * @Description 大于等于
     * @Author Silwings
     * @Date 2022/11/7 21:34
     * @Since
     **/
    public static class GreaterEqualOperator extends AbstractDynamicValue implements DynamicValue {

        public GreaterEqualOperator(final DynamicValue param) {
            super(param);
        }

        @Override
        public Boolean interpret(final Context parameterContext) {
            final List<Object> paramList = this.getParams(parameterContext);
            if (paramList.size() < 2) {
                throw new DynamicDataException("参数长度错误,需要 2,实际 " + paramList.size());
            }

            return TypeUtils.toBigDecimal(paramList.get(0)).compareTo(TypeUtils.toBigDecimal(paramList.get(1))) >= 0;
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