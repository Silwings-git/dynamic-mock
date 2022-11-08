package top.silwings.core.dynamic.expression;

import top.silwings.core.ParameterContext;
import top.silwings.core.dynamic.DynamicValue;

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
    public List<Object> value(final ParameterContext context) {
        return this.getParams(context);
    }

}