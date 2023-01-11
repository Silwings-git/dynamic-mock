package top.silwings.core.interpreter.json;

import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ArrayNode
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 15:22
 * @Since
 **/
public class ArrayNode implements ExpressionTreeNode {

    private final List<ExpressionTreeNode> expressionList;

    public ArrayNode() {
        this.expressionList = new ArrayList<>();
    }

    public ArrayNode add(final ExpressionTreeNode expression) {
        this.expressionList.add(expression);
        return this;
    }

    @Override
    public Object interpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
        return childNodeValueList;
    }

    @Override
    public List<ExpressionTreeNode> getChildNodes() {
        return this.expressionList;
    }

    @Override
    public int getNodeCount() {
        return this.expressionList.size();
    }
}