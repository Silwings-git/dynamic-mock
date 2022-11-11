package top.silwings.core.handler.dynamic.function.operations;

import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.AbstractFunctionDynamicValue;
import top.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName LessExtOperation
 * @Description 小于
 * @Author Silwings
 * @Date 2022/11/7 21:34
 * @Since
 **/
public class LessExtOperation extends AbstractFunctionDynamicValue implements DynamicValue {

    public LessExtOperation(final DynamicValue param) {
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
}