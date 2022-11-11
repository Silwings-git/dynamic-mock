package top.silwings.core.handler.dynamic.expression;

import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;

/**
 * @ClassName SingleValueExpressionDynamicValue
 * @Description 单值表达式
 * @Author Silwings
 * @Date 2022/11/8 20:23
 * @Since
 **/
public class SingleValueExpressionDynamicValue implements DynamicValue {

    private final String content;

    public SingleValueExpressionDynamicValue(final String content) {
        this.content = content;
    }

    public static DynamicValue from(final String param) {
        return new SingleValueExpressionDynamicValue(param);
    }

    @Override
    public Object interpret(final Context context) {
        return this.content;
    }

}