package top.silwings.core.dynamic.operation.extendeds;

import top.silwings.core.dynamic.DynamicValue;
import top.silwings.core.dynamic.operation.AbstractOperationDynamicValue;
import top.silwings.core.dynamic.operation.ExtendedOperation;

import java.util.List;

/**
 * @ClassName AbstractLogicExtOperation
 * @Description 逻辑运算符
 * @Author Silwings
 * @Date 2022/11/7 23:16
 * @Since
 **/
public abstract class AbstractLogicExtOperation extends AbstractOperationDynamicValue {

    protected AbstractLogicExtOperation(final List<DynamicValue> paramList) {
        super(paramList);
    }

    @Override
    public int getPriority() {
        return ExtendedOperation.LOGICAL_OPERATOR_PRIORITY;
    }
}