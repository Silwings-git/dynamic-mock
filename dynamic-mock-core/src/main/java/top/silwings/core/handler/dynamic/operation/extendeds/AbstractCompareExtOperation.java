package top.silwings.core.handler.dynamic.operation.extendeds;

import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.operation.AbstractOperationDynamicValue;
import top.silwings.core.handler.dynamic.operation.ExtendedOperation;

import java.util.List;


/**
 * @ClassName AbstractCompareExtOperation
 * @Description 比较运算符
 * @Author Silwings
 * @Date 2022/11/7 23:14
 * @Since
 **/
public abstract class AbstractCompareExtOperation extends AbstractOperationDynamicValue {

    protected AbstractCompareExtOperation(final List<DynamicValue> paramList) {
        super(paramList);
    }

}