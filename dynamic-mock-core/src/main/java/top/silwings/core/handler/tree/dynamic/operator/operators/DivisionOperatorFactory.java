package top.silwings.core.handler.tree.dynamic.operator.operators;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.operator.OperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.OperatorType;
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
        return new DivisionOperator(dynamicValueList);
    }

    /**
     * @ClassName DivisionExtOperation
     * @Description 除法
     * @Author Silwings
     * @Date 2022/11/7 21:27
     * @Since
     **/
    public static class DivisionOperator extends AbstractDynamicValue {

        public DivisionOperator(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        @Override
        public Object interpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount()) {
                throw new DynamicDataException("参数长度错误,需要 2,实际 " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).divide(TypeUtils.toBigDecimal(childNodeValueList.get(1)), MathContext.DECIMAL64);
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