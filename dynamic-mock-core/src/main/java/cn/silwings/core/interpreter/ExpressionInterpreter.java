package cn.silwings.core.interpreter;

import lombok.Getter;
import cn.silwings.core.exceptions.DynamicMockException;
import cn.silwings.core.handler.context.MockHandlerContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * @ClassName ExpressionInterpreter
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 17:21
 * @Since
 **/
@Getter
public class ExpressionInterpreter {

    private final ExpressionTreeNode expression;
    private final List<ExpressionTreeNode> expressionFlattenedList;

    public ExpressionInterpreter(final ExpressionTreeNode expression) {
        this.expression = expression;
        this.expressionFlattenedList = TreeNodeReader.postOrderTraversal(expression);
    }

    public Object interpret(final MockHandlerContext mockHandlerContext) {

        final Stack<Object> stack = new Stack<>();

        for (final ExpressionTreeNode expression : this.expressionFlattenedList) {

            final int nodeCount = expression.getNodeCount();

            if (stack.size() < nodeCount) {
                throw new DynamicMockException("The number of nodes is not as expected. expect : >=" + nodeCount + " .actual: " + stack.size());
            }

            final List<Object> arrayList = new ArrayList<>();
            if (nodeCount > 0) {
                for (int i = 0; i < nodeCount; i++) {
                    arrayList.add(stack.pop());
                }
            }
            Collections.reverse(arrayList);

            final Object interpret = expression.interpret(mockHandlerContext, arrayList);

            stack.push(interpret);
        }


        if (stack.size() != 1) {
            throw new DynamicMockException("Missing root node");
        }

        return stack.pop();
    }

}