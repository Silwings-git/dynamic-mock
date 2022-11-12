package top.silwings.core.handler.node;

import top.silwings.core.handler.Context;

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