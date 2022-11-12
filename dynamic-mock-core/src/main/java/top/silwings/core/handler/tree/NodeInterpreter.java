package top.silwings.core.handler.tree;

import lombok.Getter;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * @ClassName NodeTree
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 17:21
 * @Since
 **/
@Getter
public class NodeInterpreter {

    private final Node tree;
    private final List<Node> nodeList;

    public NodeInterpreter(final Node tree) {
        this.tree = tree;
        this.nodeList = NodeReader.postOrderTraversal(tree);
    }

    public Object interpret(final Context context) {

        final Stack<Object> stack = new Stack<>();

        for (final Node node : this.nodeList) {

            final int nodeCount = node.getNodeCount();

            if (stack.size() < nodeCount) {
                throw new DynamicDataException("缺少参数");
            }

            final List<Object> arrayList = new ArrayList<>();
            if (nodeCount > 0) {
                for (int i = 0; i < nodeCount; i++) {
                    arrayList.add(stack.pop());
                }
            }
            Collections.reverse(arrayList);

            final Object interpret = node.interpret(context, arrayList);

            stack.push(interpret);
        }


        if (stack.size() != 1) {
            throw new DynamicDataException();
        }

        return stack.pop();
    }

}