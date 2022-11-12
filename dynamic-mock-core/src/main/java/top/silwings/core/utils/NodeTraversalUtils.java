package top.silwings.core.utils;

import top.silwings.core.handler.tree.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * @ClassName NodeTraversalUtils
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 9:01
 * @Since
 **/
public class NodeTraversalUtils {

    public static List<Node> postOrderTraversal(final Node root) {

        // 如果根节点为空，则直接返回空列表
        if (root == null) {
            return Collections.emptyList();
        }

        //声明列表
        final List<Node> list = new ArrayList<>();

        //声明栈A
        final Stack<Node> stackA = new Stack<>();

        //声明栈B
        final Stack<Node> stackB = new Stack<>();

        //将次元素压入栈A
        stackA.push(root);

        //当栈A不为空时
        while (!stackA.empty()) {

            //取出其中压入的元素
            final Node node = stackA.pop();

            //压入栈B中
            stackB.push(node);

            for (Node treeNode : node.getChildNodes()) {
                stackA.push(treeNode);
            }
        }

        //当栈B不为空时
        while (!stackB.empty()) {

            //取出其元素并且添加至列表中
            final Node node = stackB.pop();

            list.add(node);

        }

        //最后返回列表
        return list;
    }

}