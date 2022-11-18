package top.silwings.core.handler.tree.dynamic.operator.operators;

import com.alibaba.fastjson2.util.TypeUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.operator.OperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.OperatorType;
import top.silwings.core.utils.CheckUtils;

import java.util.List;

/**
 * @ClassName GreaterOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:51
 * @Since
 **/
@Component
public class GreaterOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = ">";

    @Override
    public boolean support(final String symbol) {
        return ">".equals(symbol);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return GreaterOperator.from(dynamicValueList);
    }

    /**
     * @ClassName GreaterExtOperation
     * @Description 大于
     * @Author Silwings
     * @Date 2022/11/7 21:33
     * @Since
     **/
    public static class GreaterOperator extends AbstractDynamicValue {

        private GreaterOperator(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static GreaterOperator from(final List<DynamicValue> dynamicValueList) {
            CheckUtils.hasEqualsSize(dynamicValueList, 2, () -> DynamicValueCompileException.from("Operator `>` requires 2 arguments."));
            return new GreaterOperator(dynamicValueList);
        }

        @Override
        public Object doInterpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount() || this.getNodeCount() != 2) {
                throw new DynamicMockException("Parameter incorrectly of `>` operator. expect: 2, actual: " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).compareTo(TypeUtils.toBigDecimal(childNodeValueList.get(1))) > 0;
        }

        @Override
        protected String symbol() {
            return SYMBOL;
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