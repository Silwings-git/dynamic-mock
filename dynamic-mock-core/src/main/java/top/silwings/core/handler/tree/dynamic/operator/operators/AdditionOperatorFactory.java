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
 * @ClassName AdditionOperatorFactory
 * @Description 加法
 * @Author Silwings
 * @Date 2022/11/11 22:42
 * @Since
 **/
@Component
public class AdditionOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = "+";

    @Override
    public boolean support(final String symbol) {
        return "+".equals(symbol);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> param) {
        return new AdditionOperator(param);
    }

    /**
     * @ClassName AdditionExtOperation
     * @Description 加法
     * @Author Silwings
     * @Date 2022/11/7 21:20
     * @Since
     **/
    public static class AdditionOperator extends AbstractDynamicValue {

        public AdditionOperator(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        @Override
        public Object interpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount()) {
                throw new DynamicDataException("参数长度错误,需要 2,实际 " + childNodeValueList.size());
            }

            try {
                return TypeUtils.toBigDecimal(childNodeValueList.get(0)).add(TypeUtils.toBigDecimal(childNodeValueList.get(1)));
            } catch (Exception e) {
                return new StringBuilder().append(childNodeValueList.get(0)).append(childNodeValueList.get(1));
            }
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