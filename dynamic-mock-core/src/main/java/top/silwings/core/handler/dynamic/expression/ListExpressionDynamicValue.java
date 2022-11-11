package top.silwings.core.handler.dynamic.expression;

import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;

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
public class ListExpressionDynamicValue extends AbstractExpressionDynamicValue {

    public ListExpressionDynamicValue(final List<DynamicValue> paramsList) {
        super(paramsList);
    }

    public static ListExpressionDynamicValue from(final List<DynamicValue> expressions) {
        return new ListExpressionDynamicValue(expressions);
    }

    public static DynamicValue of(final DynamicValue firstValue, final DynamicValue secondValue) {
        return new ListExpressionDynamicValue(Stream.of(firstValue, secondValue).collect(Collectors.toList()));
    }

    @Override
    public List<Object> interpret(final Context context) {
        return this.getParams(context);
    }

}