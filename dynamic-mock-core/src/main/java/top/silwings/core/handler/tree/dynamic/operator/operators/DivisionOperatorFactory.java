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

import java.math.MathContext;
import java.util.List;

/**
 * @ClassName DivisionOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:49
 * @Since
 **/
@Component
public class DivisionOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = "/";

    @Override
    public boolean support(final String methodName) {
        return "/".equals(methodName);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return DivisionOperator.from(dynamicValueList);
    }

    /**
     * @ClassName DivisionExtOperation
     * @Description 除法
     * @Author Silwings
     * @Date 2022/11/7 21:27
     * @Since
     **/
    public static class DivisionOperator extends AbstractDynamicValue {

        private DivisionOperator(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static DivisionOperator from(final List<DynamicValue> dynamicValueList) {
            CheckUtils.hasEqualsSize(dynamicValueList, 2, () -> DynamicValueCompileException.from("The operator `/` requires 2 arguments."));
            return new DivisionOperator(dynamicValueList);
        }

        @Override
        public Object doInterpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount() || this.getNodeCount() != 2) {
                throw new DynamicMockException("Parameter incorrectly of `/` operator. expect: 2, actual: " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).divide(TypeUtils.toBigDecimal(childNodeValueList.get(1)), MathContext.DECIMAL64);
        }

        @Override
        protected String symbol() {
            return SYMBOL;
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