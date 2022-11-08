package top.silwings.core.node;

import top.silwings.core.Context;

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

    private static final String LEFT_BRACKET = "[";

    private static final String RIGHT_BRACKET = "]";

    private static final String COMMA = ",";

    private final List<Node> nodeList;

    public ArrayNode() {
        this.nodeList = new ArrayList<>();
    }

    public ArrayNode add(final Node node) {
        this.nodeList.add(node);
        return this;
    }

    @Override
    public void interpret(final Context context) {

        final StringBuilder builder = context.getBuilder();

        builder.append(LEFT_BRACKET);

        for (final Node node : this.nodeList) {
            node.interpret(context);
            builder.append(COMMA);
        }

        if (!this.nodeList.isEmpty()) {
            // 删除最后的逗号
            builder.deleteCharAt(builder.length() - 1);
        }

        builder.append(RIGHT_BRACKET);
    }

}