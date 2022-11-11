package top.silwings.core.handler.node;

import top.silwings.core.handler.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<Object> interpret(final Context context) {
        return this.nodeList.stream().map(node -> node.interpret(context)).collect(Collectors.toList());
    }

}