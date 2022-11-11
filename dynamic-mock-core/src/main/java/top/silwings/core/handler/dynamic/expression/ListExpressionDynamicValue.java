package top.silwings.core.handler.dynamic.expression;

import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;

import java.util.List;

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

    @Override
    public List<Object> interpret(final Context context) {
        return this.getParams(context);
    }

}