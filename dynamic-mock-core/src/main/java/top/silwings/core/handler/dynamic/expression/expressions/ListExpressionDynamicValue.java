package top.silwings.core.handler.dynamic.expression.expressions;

import org.apache.commons.collections4.CollectionUtils;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName ExpressionDynamicValue
 * @Description 表达式
 * @Author Silwings
 * @Date 2022/11/7 20:53
 * @Since
 **/
public class ListExpressionDynamicValue implements DynamicValue {

    private final List<DynamicValue> paramsList;

    public ListExpressionDynamicValue(final List<DynamicValue> paramsList) {
        this.paramsList = paramsList;
    }

    public static ListExpressionDynamicValue from(final List<DynamicValue> expressions) {
        return new ListExpressionDynamicValue(expressions);
    }

    public static DynamicValue of(final DynamicValue firstValue, final DynamicValue secondValue) {
        return new ListExpressionDynamicValue(Stream.of(firstValue, secondValue).collect(Collectors.toList()));
    }

    @Override
    public List<Object> interpret(final Context context) {
        if (CollectionUtils.isEmpty(this.paramsList)) {
            return Collections.emptyList();
        }
        return this.paramsList.stream()
                .map(dynamicValue -> dynamicValue.interpret(context))
                .collect(Collectors.toList());
    }

}