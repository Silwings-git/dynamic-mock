package top.silwings.core.interpreter;

import top.silwings.core.config.DynamicMockContext;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.expression.DynamicExpressionFactory;
import top.silwings.core.interpreter.expression.function.function_factory.SearchFunctionFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @ClassName CustomizeSpaceInterpreter
 * @Description customizeSpace专用解释器, 主要用于解决其中的嵌套search
 * @Author Silwings
 * @Date 2022/12/10 18:39
 * @Since
 **/
public class CustomizeSpaceInterpreter extends ExpressionInterpreter {

    private final Map<String, Object> customizeSpace;

    public CustomizeSpaceInterpreter(final Map<String, Object> customizeSpace, final Expression tree) {
        super(tree);
        this.customizeSpace = customizeSpace;
    }

    @Override
    public Object interpret(final MockHandlerContext mockHandlerContext) {

        final Stack<Object> stack = new Stack<>();

        mockHandlerContext.getRequestContext().setCustomizeSpace(this.customizeSpace);

        for (final Expression expression : this.getExpressionFlattenedList()) {

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

            if (expression instanceof SearchFunctionFactory.SearchFunction && arrayList.size() > 0) {

                stack.push(this.initCustomizeSpaceField(interpret, mockHandlerContext, String.valueOf(arrayList.get(0))));

            } else {

                stack.push(interpret);
            }
        }

        if (stack.size() != 1) {
            throw new DynamicMockException("Missing root node");
        }

        return stack.pop();
    }

    private Object initCustomizeSpaceField(final Object searchResult, final MockHandlerContext mockHandlerContext, final String searchKey) {

        Object interpret = searchResult;

        if (interpret instanceof String && DynamicExpressionFactory.isDynamic((String) interpret)) {

            final DynamicExpressionFactory dynamicExpressionFactory = DynamicMockContext.getInstance().getDynamicExpressionFactory();

            final Expression functionExpression = dynamicExpressionFactory.buildDynamicValue((String) interpret);

            interpret = new ExpressionInterpreter(functionExpression).interpret(mockHandlerContext);

            mockHandlerContext.getRequestContext().getCustomizeSpace().put(searchKey, interpret);
        }

        return interpret;
    }
}