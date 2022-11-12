package top.silwings.core.handler.node;

import top.silwings.core.handler.Context;
import top.silwings.core.handler.dynamic.DynamicValue;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName StaticTextNode
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 15:35
 * @Since
 **/
public class ValueNode implements Node {

    private final Object context;

    public ValueNode(final Object context) {
        this.context = context;
    }

    @Override
    public Object interpret(final Context context) {

        if (this.context instanceof DynamicValue) {
            return ((DynamicValue) this.context).interpret(context);
        }

        return this.context;
    }

    @Override
    public List<? extends Node> getChildNodes() {
        return Collections.emptyList();
    }

}