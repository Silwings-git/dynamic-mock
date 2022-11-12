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
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return new AndOperator(dynamicValueList);
    }

    /**
     * @ClassName AndDoubleExtOperation
     * @Description 双与
     * @Author Silwings
     * @Date 2022/11/7 21:37
     * @Since
     **/
    public static class AndOperator extends AbstractDynamicValue {

        public AndOperator(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        @Override
        public Object interpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount()) {
                throw new DynamicDataException("参数长度错误,需要 2,实际 " + childNodeValueList.size());
            }

            return TypeUtils.toBoolean(childNodeValueList.get(0)) && TypeUtils.toBoolean(childNodeValueList.get(1));
        }

    }

    @Override
    public int getPriority() {
        return OperatorType.LOGICAL.getPriority();
    }

    @Override
    public String getOperatorSymbol() {
        return SYMBOL;
    }
}