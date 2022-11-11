package top.silwings.core.handler.dynamic.function.operations;

import org.apache.commons.collections4.CollectionUtils;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.AbstractFunctionDynamicValue;
import top.silwings.core.utils.TypeUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName SubtractionExtOperation
 * @Description 减法
 * @Author Silwings
 * @Date 2022/11/7 21:24
 * @Since
 **/
public class SubtractionExtOperation extends AbstractFunctionDynamicValue implements DynamicValue {

    public SubtractionExtOperation(final DynamicValue param) {
        super(param);
    }

    @Override
    public BigDecimal interpret(final Context parameterContext) {
        final List<Object> paramList = this.getParams(parameterContext);
        if (CollectionUtils.isEmpty(paramList)) {
            throw new DynamicDataException("参数长度错误,需要至少 1,实际 " + paramList.size());
        }

        if (paramList.size() == 1) {
            return BigDecimal.ZERO.subtract(TypeUtils.toBigDecimal(paramList.get(0)));
        }

        return TypeUtils.toBigDecimal(paramList.get(0)).subtract(TypeUtils.toBigDecimal(paramList.get(1)));
    }

}