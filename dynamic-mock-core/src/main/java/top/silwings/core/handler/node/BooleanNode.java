package top.silwings.core.handler.node;

import top.silwings.core.handler.Context;

/**
 * @ClassName BooleanNode
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 17:06
 * @Since
 **/
public class BooleanNode implements Node {

    private final Boolean bol;

    public BooleanNode(final boolean bol) {
        this.bol = bol;
    }

    @Override
    public void interpret(final Context context) {
        context.getBuilder().append(this.bol);
    }
}