package top.silwings.core.interpreter.json;

import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ArrayNode
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 15:22
 * @Since
 **/
public class ArrayNode implements Expression {

    private final List<Expression> expressionList;

    public ArrayNode() {
        this.expressionList = new ArrayList<>();
    }

    public ArrayNode add(final Expression expression) {
        this.expressionList.add(expression);
        return this;
    }

    @Override
    public Object interpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
        return childNodeValueList;
    }

    @Override
    public List<Expression> getChildNodes() {
        return this.expressionList;
    }

    @Override
    public int getNodeCount() {
        return this.expressionList.size();
    }
}