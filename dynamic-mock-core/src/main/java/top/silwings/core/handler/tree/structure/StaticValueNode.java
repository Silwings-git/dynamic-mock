package top.silwings.core.handler.tree.structure;

import top.silwings.core.handler.Context;
import top.silwings.core.handler.tree.Node;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName StaticValueNode
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 14:58
 * @Since
 **/
public class StaticValueNode implements Node {

    private final Object context;

    public StaticValueNode(final Object context) {
        this.context = context;
    }

    public static Node from(final Object obj) {
        return new StaticValueNode(obj);
    }

    @Override
    public Object interpret(final Context context, final List<Object> childNodeValueList) {
        return this.context;
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