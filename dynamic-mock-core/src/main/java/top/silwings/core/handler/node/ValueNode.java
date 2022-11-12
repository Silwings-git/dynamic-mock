package top.silwings.core.handler.node;

import top.silwings.core.handler.Context;

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

    private final Node context;

    public ValueNode(final Node context) {
        this.context = context;
    }

    @Override
    public Object interpret(final Context context, final List<Object> childNodeValueList) {
        return childNodeValueList.get(0);
    }

    @Override
    public List<Node> getChildNodes() {
        return Collections.singletonList(this.context);
    }

    @Override
    public int getNodeCount() {
        return this.getChildNodes().size();
    }
}