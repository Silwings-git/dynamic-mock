package top.silwings.core.handler.tree.structure;

import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ArrayNode
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 15:22
 * @Since
 **/
public class ArrayNode implements Node {

    private final List<Node> nodeList;

    public ArrayNode() {
        this.nodeList = new ArrayList<>();
    }

    public ArrayNode add(final Node node) {
        this.nodeList.add(node);
        return this;
    }

    @Override
    public Object interpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
        return childNodeValueList;
    }

    @Override
    public List<Node> getChildNodes() {
        return this.nodeList;
    }

    @Override
    public int getNodeCount() {
        return this.nodeList.size();
    }
}