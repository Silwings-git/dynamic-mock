package top.silwings.core.interpreter;

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
public class TreeNodeReader {

    // 后序遍历树
    public static <T extends TreeNode<T>> List<T> postOrderTraversal(final T root) {

        if (root == null) {
            return Collections.emptyList();
        }

        final List<T> list = new ArrayList<>();
        final Stack<T> stackA = new Stack<>();
        final Stack<T> stackB = new Stack<>();

        // 将遍历到的根节点压入A栈
        stackA.push(root);

        while (!stackA.empty()) {

            // A栈栈顶元素移动到B栈
            final T expression = stackA.pop();
            stackB.push(expression);

            // 将该元素的所有子节点压A栈
            for (T treeExpression : expression.getChildNodes()) {
                stackA.push(treeExpression);
            }
        }

        // 去除B栈全部元素
        while (!stackB.empty()) {
            final T expression = stackB.pop();
            list.add(expression);
        }

        return list;
    }

}