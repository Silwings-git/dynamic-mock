package top.silwings.core.handler.tree;

import lombok.Getter;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.MockHandlerContext;

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

    public Object interpret(final MockHandlerContext mockHandlerContext) {

        final Stack<Object> stack = new Stack<>();

        for (final Node node : this.nodeList) {

            final int nodeCount = node.getNodeCount();

            if (stack.size() < nodeCount) {
                throw new DynamicMockException("The number of nodes is not as expected. expect : >=" + nodeCount + " .actual: " + stack.size());
            }

            final List<Object> arrayList = new ArrayList<>();
            if (nodeCount > 0) {
                for (int i = 0; i < nodeCount; i++) {
                    arrayList.add(stack.pop());
                }
            }
            Collections.reverse(arrayList);

            final Object interpret = node.interpret(mockHandlerContext, arrayList);

            stack.push(interpret);
        }


        if (stack.size() != 1) {
            throw new DynamicMockException("Missing root node");
        }

        return stack.pop();
    }

}