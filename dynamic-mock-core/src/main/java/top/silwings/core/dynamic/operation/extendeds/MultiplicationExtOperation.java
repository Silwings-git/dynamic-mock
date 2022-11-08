package top.silwings.core.dynamic.operation.extendeds;

import top.silwings.core.ParameterContext;
import top.silwings.core.dynamic.DynamicValue;
import top.silwings.core.dynamic.operation.AbstractOperationDynamicValue;
import top.silwings.core.dynamic.operation.ExtendedOperation;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.utils.TypeUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName MultiplicationExtOperation
 * @Description 乘法
 * @Author Silwings
 * @Date 2022/11/7 21:26
 * @Since
 **/
public class MultiplicationExtOperation extends AbstractOperationDynamicValue {

    public MultiplicationExtOperation(final List<DynamicValue> paramList) {
        super(paramList);
    }

    @Override
    public BigDecimal value(final ParameterContext parameterContext) {
        final List<Object> paramList = this.getParams(parameterContext);
        if (paramList.size() < 2) {
            throw new DynamicDataException("参数长度错误,需要 2,实际 " + paramList.size());
        }

        return TypeUtils.toBigDecimal(paramList.get(0)).multiply(TypeUtils.toBigDecimal(paramList.get(1)));
    }

    @Override
    public int getPriority() {
        return ExtendedOperation.MULTIPLICATION.getPriority();
    }
}