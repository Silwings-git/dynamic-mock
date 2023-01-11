package top.silwings.core.interpreter.json;

import lombok.Getter;
import lombok.Setter;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.Expression;

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
public class ObjectNode implements Expression {

    private final List<BinaryTreeExpression> binaryTreeNodeList;

    public ObjectNode() {
        this.binaryTreeNodeList = new ArrayList<>();
    }

    public ObjectNode put(final Expression key, final Expression value) {
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
    public List<Expression> getChildNodes() {
        return new ArrayList<>(this.binaryTreeNodeList);
    }

    @Override
    public int getNodeCount() {
        return this.binaryTreeNodeList.size();
    }

    @Getter
    @Setter
    public static class BinaryTreeExpression implements Expression {

        /**
         * 左子树节点
         */
        private Expression left;

        /**
         * 右子树节点
         */
        private Expression right;

        @Override
        public List<Expression> getChildNodes() {
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