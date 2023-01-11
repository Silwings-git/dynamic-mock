package top.silwings.core.interpreter.json;

import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName StaticValueNode
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 14:58
 * @Since
 **/
public class StaticValueNode implements ExpressionTreeNode {

    private final Object context;

    public StaticValueNode(final Object context) {
        this.context = context;
    }

    public static StaticValueNode from(final Object obj) {
        return new StaticValueNode(obj);
    }

    @Override
    public Object interpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
        return this.context;
    }

    @Override
    public List<ExpressionTreeNode> getChildNodes() {
        return Collections.emptyList();
    }

    @Override
    public int getNodeCount() {
        return 0;
    }
}