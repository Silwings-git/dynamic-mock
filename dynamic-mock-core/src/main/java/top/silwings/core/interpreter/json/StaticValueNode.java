package top.silwings.core.interpreter.json;

import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.Expression;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName StaticValueNode
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 14:58
 * @Since
 **/
public class StaticValueNode implements Expression {

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
    public List<Expression> getChildNodes() {
        return Collections.emptyList();
    }

    @Override
    public int getNodeCount() {
        return 0;
    }
}