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
 * @ClassName AndOperatorFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/11 22:44
 * @Since
 **/
@Component
public class AndOperatorFactory implements OperatorFactory {

    private static final String SYMBOL = "&&";

    @Override
    public boolean support(final String symbol) {
        return "&&".equals(symbol);
    }

    @Override
    public DynamicValue buildFunction(final DynamicValue param) {
        return new AndOperator(param);
    }

    /**
     * @ClassName AndDoubleExtOperation
     * @Description 双与
     * @Author Silwings
     * @Date 2022/11/7 21:37
     * @Since
     **/
    public static class AndOperator extends AbstractDynamicValue implements DynamicValue {

        public AndOperator(final DynamicValue param) {
            super(param);
        }

        @Override
        public Object interpret(final Context parameterContext) {

            final List<Object> paramList = this.getParams(parameterContext);
            if (paramList.size() < 2) {
                throw new DynamicDataException("参数长度错误,需要 2,实际 " + paramList.size());
            }

            return TypeUtils.toBoolean(paramList.get(0)) && TypeUtils.toBoolean(paramList.get(1));
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