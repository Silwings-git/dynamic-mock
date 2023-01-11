package top.silwings.core.interpreter.dynamic_expression.terminal;

import org.apache.commons.collections4.CollectionUtils;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionTreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName ExpressionDynamicValue
 * @Description 表达式
 * @Author Silwings
 * @Date 2022/11/7 20:53
 * @Since
 **/
public class MultipleTerminalExpression implements ExpressionTreeNode {

    private final List<ExpressionTreeNode> paramsList;

    public MultipleTerminalExpression(final List<ExpressionTreeNode> paramsList) {
        this.paramsList = paramsList;
    }

    public static MultipleTerminalExpression from(final List<ExpressionTreeNode> expressions) {
        return new MultipleTerminalExpression(expressions);
    }

    @Override
    public List<Object> interpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
        return CollectionUtils.isEmpty(childNodeValueList) ? Collections.emptyList() : childNodeValueList;
    }

    @Override
    public List<ExpressionTreeNode> getChildNodes() {
        return new ArrayList<>(this.paramsList);
    }

    @Override
    public int getNodeCount() {
        return this.paramsList.size();
    }

    public List<ExpressionTreeNode> getCommaExpressionValue() {
        return this.paramsList;
    }
}