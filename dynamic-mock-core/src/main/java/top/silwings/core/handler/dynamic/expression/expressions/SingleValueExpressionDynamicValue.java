package top.silwings.core.handler.dynamic.expression.expressions;

import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.node.Node;

import java.util.Collections;
import java.util.List;

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
    public Object interpret(final Context context, final List<Object> childNodeValueList) {
        return this.content;
    }

    @Override
    public List<Node> getChildNodes() {
        return Collections.emptyList();
    }

    @Override
    public int getNodeCount() {
        return this.getChildNodes().size();
    }

}