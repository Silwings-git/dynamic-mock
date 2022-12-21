package top.silwings.core.handler.tree.dynamic.operator.operators;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.operator.OperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.OperatorType;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName RemainderOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:58
 * @Since
 **/
@Component
public class RemainderOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = "%";

    @Override
    public boolean support(final String symbol) {
        return "%".equals(symbol);
    }

    @Override
    public DynamicValue buildDynamicValue(final List<DynamicValue> dynamicValueList) {
        return RemainderOperator.from(dynamicValueList);
    }

    @Override
    public int getPriority() {
        return OperatorType.ARITHMETIC_TWO.getPriority();
    }

    @Override
    public String getOperatorSymbol() {
        return SYMBOL;
    }

    /**
     * @ClassName RemainderExtOperation
     * @Description 模
     * @Author Silwings
     * @Date 2022/11/7 21:28
     * @Since
     **/
    public static class RemainderOperator extends AbstractDynamicValue {

        private RemainderOperator(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static RemainderOperator from(final List<DynamicValue> dynamicValueList) {
            CheckUtils.hasEqualsSize(dynamicValueList, 2, DynamicValueCompileException.supplier("The operator `%` requires 2 arguments."));
            return new RemainderOperator(dynamicValueList);
        }

        @Override
        public Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount() || this.getNodeCount() != 2) {
                throw new DynamicMockException("Parameter incorrectly of `%` operator. expect: 2, actual: " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).divideAndRemainder(TypeUtils.toBigDecimal(childNodeValueList.get(1)))[1];
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }

    }
}