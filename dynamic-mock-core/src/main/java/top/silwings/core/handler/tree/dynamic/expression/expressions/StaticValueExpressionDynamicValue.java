package top.silwings.core.handler.tree.dynamic.expression.expressions;

import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.structure.StaticValueNode;

/**
 * @ClassName SingleValueExpressionDynamicValue
 * @Description 单值表达式
 * @Author Silwings
 * @Date 2022/11/8 20:23
 * @Since
 **/
public class StaticValueExpressionDynamicValue extends StaticValueNode implements DynamicValue {

    public StaticValueExpressionDynamicValue(final Object content) {
        super(content);
    }

    public static DynamicValue from(final Object param) {
        return new StaticValueExpressionDynamicValue(param);
    }

}