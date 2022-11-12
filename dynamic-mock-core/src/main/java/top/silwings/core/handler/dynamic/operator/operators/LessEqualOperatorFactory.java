package top.silwings.core.handler.dynamic.operator.operators;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.operator.OperatorFactory;
import top.silwings.core.handler.dynamic.operator.OperatorType;
import top.silwings.core.utils.TypeUtils;

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
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return new LessEqualOperator(dynamicValueList);
    }

    /**
     * @ClassName LessEqualExtOperation
     * @Description 小于等于
     * @Author Silwings
     * @Date 2022/11/7 21:35
     * @Since
     **/
    public static class LessEqualOperator extends AbstractDynamicValue {

        public LessEqualOperator(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        @Override
        public Object interpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount()) {
                throw new DynamicDataException("参数长度错误,需要 2,实际 " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).compareTo(TypeUtils.toBigDecimal(childNodeValueList.get(1))) <= 0;
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