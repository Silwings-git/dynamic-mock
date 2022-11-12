package top.silwings.core.handler.dynamic.expression.expressions;

import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.node.StaticValueNode;

/**
 * @ClassName SingleValueExpressionDynamicValue
 * @Description 单值表达式
 * @Author Silwings
 * @Date 2022/11/8 20:23
 * @Since
 **/
public class StaticValueExpressionDynamicValue extends StaticValueNode implements DynamicValue {

    public StaticValueExpressionDynamicValue(final String content) {
        super(content);
    }

    public static DynamicValue from(final String param) {
        return new StaticValueExpressionDynamicValue(param);
    }

}