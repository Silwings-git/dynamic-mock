package top.silwings.core.handler.dynamic.operator.operators;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.operator.OperatorFactory;
import top.silwings.core.handler.dynamic.operator.OperatorType;
import top.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName LessOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:54
 * @Since
 **/
@Component
public class LessOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = "<";

    @Override
    public boolean support(final String symbol) {
        return SYMBOL.equals(symbol);
    }

    @Override
    public DynamicValue buildFunction(final DynamicValue param) {
        return new LessOperator(param);
    }

    /**
     * @ClassName LessExtOperation
     * @Description 小于
     * @Author Silwings
     * @Date 2022/11/7 21:34
     * @Since
     **/
    public static class LessOperator extends AbstractDynamicValue implements DynamicValue {

        public LessOperator(final DynamicValue param) {
            super(param);
        }

        @Override
        public Boolean interpret(final Context parameterContext) {
            final List<Object> paramList = this.getParams(parameterContext);
            if (paramList.size() < 2) {
                throw new DynamicDataException("参数长度错误,需要 2,实际 " + paramList.size());
            }

            return TypeUtils.toBigDecimal(paramList.get(0)).compareTo(TypeUtils.toBigDecimal(paramList.get(1))) < 0;
        }

        @Override
        public Object interpret(final Context context, final List<Object> childNodeValueList) {
            if (childNodeValueList.size() < this.getNodeCount()) {
                throw new DynamicDataException("参数长度错误,需要 2,实际 " + childNodeValueList.size());
            }

            return TypeUtils.toBigDecimal(childNodeValueList.get(0)).compareTo(TypeUtils.toBigDecimal(childNodeValueList.get(1))) < 0;
        }

        @Override
        public int getNodeCount() {
            return 2;
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