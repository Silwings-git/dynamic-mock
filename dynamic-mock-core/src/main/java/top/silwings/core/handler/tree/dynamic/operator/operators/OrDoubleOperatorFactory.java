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
    public DynamicValue buildDynamicValue(final List<DynamicValue> dynamicValueList) {
        return OrOperator.from(dynamicValueList);
    }

    @Override
    public int getPriority() {
        return OperatorType.LOGICAL.getPriority();
    }

    @Override
    public String getOperatorSymbol() {
        return SYMBOL;
    }

    /**
     * @ClassName OrDoubleExtOperation
     * @Description 双或
     * @Author Silwings
     * @Date 2022/11/7 21:38
     * @Since
     **/
    public static class OrOperator extends AbstractDynamicValue {

        private OrOperator(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static OrOperator from(final List<DynamicValue> dynamicValueList) {
            CheckUtils.hasEqualsSize(dynamicValueList, 2, DynamicValueCompileException.supplier("The operator `||` requires 2 arguments."));
            return new OrOperator(dynamicValueList);
        }

        @Override
        public Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount() || this.getNodeCount() != 2) {
                throw new DynamicMockException("Parameter incorrectly of `||` operator. expect: 2, actual: " + childNodeValueList.size());
            }

            return TypeUtils.toBooleanValue(childNodeValueList.get(0)) || TypeUtils.toBooleanValue(childNodeValueList.get(1));
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }
}