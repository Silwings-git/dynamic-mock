package top.silwings.core.handler.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * @ClassName NodeReader
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 17:22
 * @Since
 **/
public class NodeReader {

    public static List<Node> postOrderTraversal(final Node root) {

        if (root == null) {
            return Collections.emptyList();
        }

        final List<Node> list = new ArrayList<>();
        final Stack<Node> stackA = new Stack<>();
        final Stack<Node> stackB = new Stack<>();

        // 将遍历到的根节点压入A栈
        stackA.push(root);

        while (!stackA.empty()) {

            // A栈栈顶元素移动到B栈
            final Node node = stackA.pop();
            stackB.push(node);

            // 将该元素的所有子节点压A栈
            for (Node treeNode : node.getChildNodes()) {
                stackA.push(treeNode);
            }
        }

        // 去除B栈全部元素
        while (!stackB.empty()) {
            final Node node = stackB.pop();
            list.add(node);
        }

        return list;
    }

}