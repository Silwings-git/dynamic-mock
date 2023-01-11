package top.silwings.core.interpreter.json;

import lombok.Getter;
import lombok.Setter;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;

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
public class ObjectNode implements ExpressionTreeNode {

    private final List<BinaryTreeExpression> binaryTreeNodeList;

    public ObjectNode() {
        this.binaryTreeNodeList = new ArrayList<>();
    }

    public ObjectNode put(final ExpressionTreeNode key, final ExpressionTreeNode value) {
        final BinaryTreeExpression treeNode = new BinaryTreeExpression();
        treeNode.setLeft(key);
        treeNode.setRight(value);
        this.binaryTreeNodeList.add(treeNode);
        return this;
    }

    @Override
    public Object interpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

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
    public List<ExpressionTreeNode> getChildNodes() {
        return new ArrayList<>(this.binaryTreeNodeList);
    }

    @Override
    public int getNodeCount() {
        return this.binaryTreeNodeList.size();
    }

    @Getter
    @Setter
    public static class BinaryTreeExpression implements ExpressionTreeNode {

        /**
         * 左子树节点
         */
        private ExpressionTreeNode left;

        /**
         * 右子树节点
         */
        private ExpressionTreeNode right;

        @Override
        public List<ExpressionTreeNode> getChildNodes() {
            return Stream.of(this.left, this.right).collect(Collectors.toList());
        }

        @Override
        public Object interpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            return childNodeValueList;
        }

        @Override
        public int getNodeCount() {
            return 2;
        }
    }
}