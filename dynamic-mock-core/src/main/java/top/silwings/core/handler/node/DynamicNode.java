package top.silwings.core.handler.node;

import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;

/**
 * @ClassName DynamicNode
 * @Description 动态节点
 * @Author Silwings
 * @Date 2022/10/29 17:41
 * @Since
 **/
public class DynamicNode implements Node {

    private final DynamicValue dynamicValue;

    public DynamicNode(final DynamicValue dynamicValue) {
        this.dynamicValue = dynamicValue;
    }

    @Override
    public Object interpret(final Context context) {
        return this.dynamicValue.value(context.getParameterContext());
    }

}