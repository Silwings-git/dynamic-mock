package top.silwings.core.handler.node;

import top.silwings.core.handler.Context;

/**
 * @ClassName StaticTextNode
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 15:35
 * @Since
 **/
public class StaticTextNode implements Node {

    private static final String QUOTATION_MARK = "\"";

    private final String context;

    public StaticTextNode(final String context) {
        this.context = context;
    }

    @Override
    public void interpret(final Context context) {
        context.getBuilder()
                .append(QUOTATION_MARK)
                .append(this.context)
                .append(QUOTATION_MARK);
    }

}