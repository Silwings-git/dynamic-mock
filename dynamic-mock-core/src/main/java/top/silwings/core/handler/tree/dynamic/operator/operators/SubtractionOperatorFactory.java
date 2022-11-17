package top.silwings.core.handler.tree.dynamic.operator.operators;

import com.alibaba.fastjson2.util.TypeUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.operator.OperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.OperatorType;

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
        public Object doInterpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount() || this.getNodeCount() != 2) {
                throw new DynamicMockException("Parameter incorrectly of `-` operator. expect: 2, actual: " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).subtract(TypeUtils.toBigDecimal(childNodeValueList.get(1)));
        }

        @Override
        protected String symbol() {
            return SYMBOL;
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