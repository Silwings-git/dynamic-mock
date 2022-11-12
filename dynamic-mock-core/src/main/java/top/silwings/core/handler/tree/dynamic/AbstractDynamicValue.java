package top.silwings.core.handler.tree.dynamic;

import top.silwings.core.handler.tree.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FunctionDynamicValue
 * @Description 函数
 * @Author Silwings
 * @Date 2022/11/7 20:52
 * @Since
 **/
public abstract class AbstractDynamicValue implements DynamicValue {

    private final List<DynamicValue> dynamicValueList;

    public AbstractDynamicValue(final List<DynamicValue> dynamicValueList) {
        this.dynamicValueList = dynamicValueList;
    }

    @Override
    public List<Node> getChildNodes() {
        return new ArrayList<>(this.dynamicValueList);
    }

    @Override
    public int getNodeCount() {
        return this.getChildNodes().size();
    }

}