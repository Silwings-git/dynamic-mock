package top.silwings.core.node;

import top.silwings.core.Context;

/**
 * @ClassName NumberNode
 * @Description
 * @Author Silwings
 * @Date 2022/10/28 15:38
 * @Since
 **/
public class NumberNode implements Node {

    private final Number number;

    public NumberNode(final Number number) {
        this.number = number;
    }

    @Override
    public void interpret(final Context context) {
        context.getBuilder().append(this.number);
    }

}