package top.silwings.core.handler.dynamic.function.operations;

import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.AbstractFunctionDynamicValue;
import top.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName OrDoubleExtOperation
 * @Description 双或
 * @Author Silwings
 * @Date 2022/11/7 21:38
 * @Since
 **/
public class OrDoubleExtOperation extends AbstractFunctionDynamicValue implements DynamicValue {

    public OrDoubleExtOperation(final DynamicValue param) {
        super(param);
    }

    @Override
    public Object interpret(final Context parameterContext) {
        final List<Object> paramList = this.getParams(parameterContext);
        if (paramList.size() < 2) {
            throw new DynamicDataException("参数长度错误,需要 2,实际 " + paramList.size());
        }

        return TypeUtils.toBoolean(paramList.get(0)) || TypeUtils.toBoolean(paramList.get(1));
    }

}