package top.silwings.core.interpreter.expression.terminal;

import org.apache.commons.collections4.CollectionUtils;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.Expression;

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
public class MultipleTerminalExpression implements Expression {

    private final List<Expression> paramsList;

    public MultipleTerminalExpression(final List<Expression> paramsList) {
        this.paramsList = paramsList;
    }

    public static MultipleTerminalExpression from(final List<Expression> expressions) {
        return new MultipleTerminalExpression(expressions);
    }

    @Override
    public List<Object> interpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
        return CollectionUtils.isEmpty(childNodeValueList) ? Collections.emptyList() : childNodeValueList;
    }

    @Override
    public List<Expression> getChildNodes() {
        return new ArrayList<>(this.paramsList);
    }

    @Override
    public int getNodeCount() {
        return this.paramsList.size();
    }

    public List<Expression> getCommaExpressionValue() {
        return this.paramsList;
    }
}