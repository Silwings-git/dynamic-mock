package top.silwings.core.handler.tree.dynamic.operator.operators;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.operator.OperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.OperatorType;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.TypeUtils;

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
        return AdditionOperator.from(param);
    }

    /**
     * @ClassName AdditionExtOperation
     * @Description 加法
     * @Author Silwings
     * @Date 2022/11/7 21:20
     * @Since
     **/
    public static class AdditionOperator extends AbstractDynamicValue {

        private AdditionOperator(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static AdditionOperator from(final List<DynamicValue> dynamicValueList) {
            CheckUtils.hasEqualsSize(dynamicValueList, 2, () -> DynamicValueCompileException.from("The operator `+` requires 2 arguments."));
            return new AdditionOperator(dynamicValueList);
        }

        @Override
        public Object doInterpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() != this.getNodeCount() || childNodeValueList.size() != 2) {
                throw new DynamicMockException("Parameter incorrectly of `+` operator. expect: 2, actual: " + childNodeValueList.size());
            }

            try {
                return TypeUtils.toBigDecimal(childNodeValueList.get(0)).add(TypeUtils.toBigDecimal(childNodeValueList.get(1)));
            } catch (Exception e) {
                return new StringBuilder().append(childNodeValueList.get(0)).append(childNodeValueList.get(1));
            }
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