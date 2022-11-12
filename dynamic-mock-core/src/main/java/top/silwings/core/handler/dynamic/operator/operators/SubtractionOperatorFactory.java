package top.silwings.core.handler.dynamic.operator.operators;

import org.apache.commons.collections4.CollectionUtils;
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
 * @ClassName SubtractionOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:59
 * @Since
 **/
@Component
public class SubtractionOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = "-";

    @Override
    public boolean support(final String symbol) {
        return "-".equals(symbol);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return new SubtractionOperator(dynamicValueList);
    }


    /**
     * @ClassName SubtractionExtOperation
     * @Description 减法
     * @Author Silwings
     * @Date 2022/11/7 21:24
     * @Since
     **/
    public static class SubtractionOperator extends AbstractDynamicValue {

        public SubtractionOperator(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        @Override
        public Object interpret(final Context context, final List<Object> childNodeValueList) {
            if (CollectionUtils.isEmpty(childNodeValueList) || childNodeValueList.size() < this.getNodeCount()) {
                throw new DynamicDataException("参数长度错误,需要至少 2,实际 " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).subtract(TypeUtils.toBigDecimal(childNodeValueList.get(1)));
        }

        @Override
        public int getNodeCount() {
            return this.getChildNodes().size();
        }
    }

    @Override
    public int getPriority() {
        return OperatorType.ARITHMETIC_ONE.getPriority();
    }

    @Override
    public String getOperatorSymbol() {
        return SYMBOL;
    }
}