package top.silwings.core.handler.dynamic.operation.extendeds;

import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.operation.AbstractOperationDynamicValue;
import top.silwings.core.handler.dynamic.operation.ExtendedOperation;

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

}