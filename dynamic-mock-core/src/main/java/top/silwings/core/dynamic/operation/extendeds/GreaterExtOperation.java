package top.silwings.core.dynamic.operation.extendeds;

import top.silwings.core.ParameterContext;
import top.silwings.core.dynamic.DynamicValue;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.utils.TypeUtils;

import java.util.List;

/**
 * @ClassName GreaterExtOperation
 * @Description 大于
 * @Author Silwings
 * @Date 2022/11/7 21:33
 * @Since
 **/
public class GreaterExtOperation extends AbstractCompareExtOperation {

    public GreaterExtOperation(final List<DynamicValue> paramList) {
        super(paramList);
    }

    @Override
    public Boolean value(final ParameterContext parameterContext) {
        final List<Object> paramList = this.getParams(parameterContext);
        if (paramList.size() < 2) {
            throw new DynamicDataException("参数长度错误,需要 2,实际 " + paramList.size());
        }

        return TypeUtils.toBigDecimal(paramList.get(0)).compareTo(TypeUtils.toBigDecimal(paramList.get(1))) > 0;
    }
}