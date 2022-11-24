package top.silwings.core.handler.tree.dynamic;

import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.TypeCastException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.Node;
import top.silwings.core.utils.JsonUtils;

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

    protected AbstractDynamicValue(final List<DynamicValue> dynamicValueList) {
        this.dynamicValueList = dynamicValueList;
    }

    @Override
    public List<Node> getChildNodes() {
        return new ArrayList<>(this.dynamicValueList);
    }

    @Override
    public int getNodeCount() {
        return this.dynamicValueList.size();
    }

    @Override
    public Object interpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
        try {
            return this.doInterpret(mockHandlerContext, childNodeValueList);
        } catch (TypeCastException e) {
            throw new DynamicMockException("Dynamic operation '" + symbol() + "' execution failed. " + e.getMessage() + ": " + JsonUtils.toJSONString(childNodeValueList), e);
        }
    }

    protected abstract Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList);

    /**
     * 返回该函数的符号
     */
    protected abstract String symbol();
}