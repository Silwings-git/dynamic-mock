package top.silwings.core.interpreter.dynamic_expression.terminal;

import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.json.StaticValueNode;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName SingleValueExpressionDynamicValue
 * @Description 单值表达式
 * @Author Silwings
 * @Date 2022/11/8 20:23
 * @Since
 **/
public class SingleTerminalNode implements ExpressionTreeNode {

    private final Object context;

    public SingleTerminalNode(final Object context) {
        this.context = context;
    }

    public static StaticValueNode from(final Object context) {
        return new StaticValueNode(context);
    }

    @Override
    public Object interpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
        return this.context;
    }

    @Override
    public int getNodeCount() {
        return 1;
    }

    @Override
    public List<ExpressionTreeNode> getChildNodes() {
        return Collections.emptyList();
    }
}