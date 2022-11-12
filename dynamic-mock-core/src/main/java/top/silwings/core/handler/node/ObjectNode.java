package top.silwings.core.handler.node;

import lombok.Getter;
import lombok.Setter;
import top.silwings.core.handler.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName ObjectNode
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 15:22
 * @Since
 **/
public class ObjectNode implements Node {

    private final List<BinaryTreeNode> binaryTreeNodeList;

    public ObjectNode() {
        this.binaryTreeNodeList = new ArrayList<>();
    }

    public ObjectNode put(final Node key, final Node value) {
        final BinaryTreeNode treeNode = new BinaryTreeNode();
        treeNode.setLeft(key);
        treeNode.setRight(value);
        this.binaryTreeNodeList.add(treeNode);
        return this;
    }

    @Override
    public Object interpret(final Context context) {

        final HashMap<Object, Object> hashMap = new HashMap<>();

        for (final BinaryTreeNode binaryTreeNode : this.binaryTreeNodeList) {
            hashMap.put(binaryTreeNode.getLeft().interpret(context), binaryTreeNode.getRight().interpret(context));
        }

        return hashMap;
    }


    @Getter
    @Setter
    public static class BinaryTreeNode implements Node {

        /**
         * 左子树节点
         */
        private Node left;

        /**
         * 右子树节点
         */
        private Node right;

        @Override
        public List<Object> interpret(final Context context) {
            return Stream.of(this.left.interpret(context), this.right.interpret(context)).collect(Collectors.toList());
        }

        @Override
        public List<Node> getChildNodes() {
            return Stream.of(this.left, this.right).collect(Collectors.toList());
        }
    }

    @Override
    public List<? extends Node> getChildNodes() {
        return this.binaryTreeNodeList;
    }

}