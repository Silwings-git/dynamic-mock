package top.silwings.core.node;

import top.silwings.core.Context;
import top.silwings.core.dynamic.DynamicValue;

/**
 * @ClassName DynamicNode
 * @Description 动态节点
 * @Author Silwings
 * @Date 2022/10/29 17:41
 * @Since
 **/
public class DynamicNode implements Node {

    private static final String QUOTATION_MARK = "\"";

    private final DynamicValue dynamicValue;

    public DynamicNode(final DynamicValue dynamicValue) {
        this.dynamicValue = dynamicValue;
    }

    @Override
    public void interpret(final Context context) {
        final Object data = this.dynamicValue.value(context.getParameterContext());
        if (data instanceof String) {
            context.getBuilder()
                    .append(QUOTATION_MARK)
                    .append(data)
                    .append(QUOTATION_MARK);
        } else {
            context.getBuilder().append(data);
        }
    }

}