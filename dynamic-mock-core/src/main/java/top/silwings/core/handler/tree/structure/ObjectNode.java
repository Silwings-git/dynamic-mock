package top.silwings.core.handler.tree.structure;

import lombok.Getter;
import lombok.Setter;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.tree.Node;

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
    public Object interpret(final Context context, final List<Object> childNodeValueList) {

        final HashMap<Object, Object> hashMap = new HashMap<>();

        for (final Object obj : childNodeValueList) {

            if (obj instanceof List) {
                final List<?> list = ((List<?>) obj);
                if (list.size() < 2) {
                    throw new DynamicMockException("Missing mapping value.");
                }
                hashMap.put(list.get(0), list.get(1));
            }

        }

        return hashMap;
    }

    @Override
    public List<Node> getChildNodes() {
        return new ArrayList<>(this.binaryTreeNodeList);
    }

    @Override
    public int getNodeCount() {
        return this.binaryTreeNodeList.size();
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
        public List<Node> getChildNodes() {
            return Stream.of(this.left, this.right).collect(Collectors.toList());
        }

        @Override
        public Object interpret(final Context context, final List<Object> childNodeValueList) {
            return childNodeValueList;
        }

        @Override
        public int getNodeCount() {
            return 2;
        }
    }
}