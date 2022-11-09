package top.silwings.core.handler.node;

import top.silwings.core.handler.Context;

/**
 * @ClassName NullNode
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 15:56
 * @Since
 **/
public class NullNode implements Node {

    public static final NullNode NULL_NODE = new NullNode();

    private NullNode() {
    }

    public static NullNode nullNode() {
        return NullNode.NULL_NODE;
    }

    @Override
    public void interpret(final Context context) {
        context.getBuilder().append((String) null);
    }
}