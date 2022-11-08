package top.silwings.core.dynamic.operation.extendeds;

import org.apache.commons.collections4.CollectionUtils;
import top.silwings.core.ParameterContext;
import top.silwings.core.dynamic.DynamicValue;
import top.silwings.core.dynamic.operation.AbstractOperationDynamicValue;
import top.silwings.core.dynamic.operation.ExtendedOperation;
import top.silwings.core.exceptions.DynamicDataException;
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
public class SubtractionExtOperation extends AbstractOperationDynamicValue {

    public SubtractionExtOperation(final List<DynamicValue> paramList) {
        super(paramList);
    }

    @Override
    public BigDecimal value(final ParameterContext parameterContext) {
        final List<Object> paramList = this.getParams(parameterContext);
        if (CollectionUtils.isEmpty(paramList)) {
            throw new DynamicDataException("参数长度错误,需要至少 1,实际 " + paramList.size());
        }

        if (paramList.size() == 1) {
            return BigDecimal.ZERO.subtract(TypeUtils.toBigDecimal(paramList.get(0)));
        }

        return TypeUtils.toBigDecimal(paramList.get(0)).subtract(TypeUtils.toBigDecimal(paramList.get(1)));
    }

    @Override
    public int getPriority() {
        return ExtendedOperation.SUBTRACTION.getPriority();
    }
}