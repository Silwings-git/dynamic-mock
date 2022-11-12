package top.silwings.core.handler.tree.dynamic.operator.operators;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.operator.OperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.OperatorType;
import top.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName OrDoubleOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:57
 * @Since
 **/
@Component
public class OrDoubleOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = "||";

    @Override
    public boolean support(final String symbol) {
        return "||".equals(symbol);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return new OrOperator(dynamicValueList);
    }

    /**
     * @ClassName OrDoubleExtOperation
     * @Description 双或
     * @Author Silwings
     * @Date 2022/11/7 21:38
     * @Since
     **/
    public static class OrOperator extends AbstractDynamicValue {

        public OrOperator(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        @Override
        public Object interpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount()) {
                throw new DynamicDataException("参数长度错误,需要 2,实际 " + childNodeValueList.size());
            }

            return TypeUtils.toBoolean(childNodeValueList.get(0)) || TypeUtils.toBoolean(childNodeValueList.get(1));
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