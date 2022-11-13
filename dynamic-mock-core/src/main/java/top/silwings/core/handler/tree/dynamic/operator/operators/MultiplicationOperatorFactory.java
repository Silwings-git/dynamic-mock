package top.silwings.core.handler.tree.dynamic.operator.operators;

import com.alibaba.fastjson2.util.TypeUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.operator.OperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.OperatorType;

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
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return new MultiplicationOperator(dynamicValueList);
    }

    /**
     * @ClassName MultiplicationExtOperation
     * @Description 乘法
     * @Author Silwings
     * @Date 2022/11/7 21:26
     * @Since
     **/
    public static class MultiplicationOperator extends AbstractDynamicValue {

        public MultiplicationOperator(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        @Override
        public Object interpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount()) {
                throw new DynamicDataException("参数长度错误,需要 2,实际 " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).multiply(TypeUtils.toBigDecimal(childNodeValueList.get(1)));
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