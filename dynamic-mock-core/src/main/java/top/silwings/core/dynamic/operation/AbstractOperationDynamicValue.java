package top.silwings.core.dynamic.operation;

import org.apache.commons.collections4.CollectionUtils;
import top.silwings.core.ParameterContext;
import top.silwings.core.dynamic.DynamicValue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName OperationDynamicValue
 * @Description 操作符
 * @Author Silwings
 * @Date 2022/11/7 21:02
 * @Since
 **/
public abstract class AbstractOperationDynamicValue implements Operator {

    // 抽象类,提供数组存储参数,子类只要负责通过parameterContext计算即可
    private final List<DynamicValue> paramList;

    protected AbstractOperationDynamicValue(final List<DynamicValue> paramList) {
        this.paramList = paramList;
    }

    protected List<Object> getParams(final ParameterContext parameterContext) {

        if (CollectionUtils.isEmpty(paramList)) {
            return Collections.emptyList();
        }

        return this.paramList.stream()
                .map(dynamicValue -> dynamicValue.value(parameterContext))
                .collect(Collectors.toList());
    }

}