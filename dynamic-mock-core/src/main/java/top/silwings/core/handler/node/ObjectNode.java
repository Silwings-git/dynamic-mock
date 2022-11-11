package top.silwings.core.handler.node;

import top.silwings.core.handler.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ObjectNode
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 15:22
 * @Since
 **/
public class ObjectNode implements Node {

    private final Map<Node, Node> nodeMap;

    public ObjectNode() {
        this.nodeMap = new HashMap<>();
    }

    public ObjectNode put(final Node key, final Node value) {
        this.nodeMap.put(key, value);
        return this;
    }

    @Override
    public Object interpret(final Context context) {

        final HashMap<Object, Object> hashMap = new HashMap<>();

        for (final Map.Entry<Node, Node> nodeEntry : this.nodeMap.entrySet()) {
            hashMap.put(nodeEntry.getKey().interpret(context), nodeEntry.getValue().interpret(context));
        }

        return hashMap;
    }

}