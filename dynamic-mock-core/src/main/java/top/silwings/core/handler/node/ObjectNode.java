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

    private static final String LEFT_BRACE = "{";

    private static final String RIGHT_BRACE = "}";

    private static final String COLON = ":";

    private static final String COMMA = ",";

    private final Map<Node, Node> nodeMap;

    public ObjectNode() {
        this.nodeMap = new HashMap<>();
    }

    public ObjectNode put(final Node key, final Node value) {
        this.nodeMap.put(key, value);
        return this;
    }

    @Override
    public void interpret(final Context context) {

        final StringBuilder builder = context.getBuilder();

        builder.append(LEFT_BRACE);

        for (final Map.Entry<Node, Node> nodeEntry : this.nodeMap.entrySet()) {

            nodeEntry.getKey().interpret(context);
            builder.append(COLON);
            nodeEntry.getValue().interpret(context);
            builder.append(COMMA);
        }

        if (!this.nodeMap.isEmpty()) {
            // 删除最后的逗号
            builder.deleteCharAt(builder.length() - 1);
        }

        builder.append(RIGHT_BRACE);
    }

}