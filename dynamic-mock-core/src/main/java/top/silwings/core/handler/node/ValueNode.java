package top.silwings.core.handler.node;

import top.silwings.core.handler.Context;

/**
 * @ClassName StaticTextNode
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 15:35
 * @Since
 **/
public class ValueNode implements Node {

    private static final String QUOTATION_MARK = "\"";

    private final Object context;

    public ValueNode(final Object context) {
        this.context = context;
    }

    @Override
    public Object interpret(final Context context) {
        return this.context;
    }

}