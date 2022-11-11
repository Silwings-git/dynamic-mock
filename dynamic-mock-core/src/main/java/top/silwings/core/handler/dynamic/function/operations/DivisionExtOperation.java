package top.silwings.core.handler.dynamic.function.operations;

import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.function.AbstractFunctionDynamicValue;
import top.silwings.core.utils.TypeUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

/**
 * @ClassName DivisionExtOperation
 * @Description 除法
 * @Author Silwings
 * @Date 2022/11/7 21:27
 * @Since
 **/
public class DivisionExtOperation extends AbstractFunctionDynamicValue implements DynamicValue {

    public DivisionExtOperation(final DynamicValue param) {
        super(param);
    }

    @Override
    public BigDecimal interpret(final Context parameterContext) {
        final List<Object> paramList = this.getParams(parameterContext);
        if (paramList.size() < 2) {
            throw new DynamicDataException("参数长度错误,需要 2,实际 " + paramList.size());
        }

        return TypeUtils.toBigDecimal(paramList.get(0)).divide(TypeUtils.toBigDecimal(paramList.get(1)), MathContext.DECIMAL64);
    }

}