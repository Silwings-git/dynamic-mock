package top.silwings.core.handler.dynamic.operation.extendeds;

import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.ParameterContext;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.operation.AbstractOperationDynamicValue;
import top.silwings.core.handler.dynamic.operation.ExtendedOperation;
import top.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName AdditionExtOperation
 * @Description 加法
 * @Author Silwings
 * @Date 2022/11/7 21:20
 * @Since
 **/
public class AdditionExtOperation extends AbstractOperationDynamicValue {

    public AdditionExtOperation(final List<DynamicValue> paramList) {
        super(paramList);
    }

    @Override
    public Object value(final ParameterContext parameterContext) {

        final List<Object> paramList = this.getParams(parameterContext);
        if (paramList.size() < 2) {
            throw new DynamicDataException("参数长度错误,需要 2,实际 " + paramList.size());
        }

        try {
            return TypeUtils.toBigDecimal(paramList.get(0)).add(TypeUtils.toBigDecimal(paramList.get(1)));
        } catch (Exception e) {
            return new StringBuilder().append(paramList.get(0)).append(paramList.get(1));
        }
    }

    @Override
    public int getPriority() {
        return ExtendedOperation.ADDITION.getPriority();
    }
}