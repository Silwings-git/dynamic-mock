package top.silwings.core.dynamic.expression;

import org.apache.commons.collections4.CollectionUtils;
import top.silwings.core.ParameterContext;
import top.silwings.core.dynamic.DynamicValue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName AbstractExpressionDynamicValue
 * @Description
 * @Author Silwings
 * @Date 2022/11/7 20:15
 * @Since
 **/
public abstract class AbstractExpressionDynamicValue implements DynamicValue {

    private final List<DynamicValue> paramsList;

    protected AbstractExpressionDynamicValue(final List<DynamicValue> paramsList) {
        this.paramsList = paramsList;
    }

    public List<Object> getParams(final ParameterContext context) {
        if (CollectionUtils.isEmpty(this.paramsList)) {
            return Collections.emptyList();
        }
        return this.paramsList.stream()
                .map(dynamicValue -> dynamicValue.value(context))
                .collect(Collectors.toList());
    }

}