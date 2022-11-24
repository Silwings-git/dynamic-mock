package top.silwings.core.handler.tree.dynamic.expression.expressions;

import org.apache.commons.collections4.CollectionUtils;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.Node;
import top.silwings.core.handler.tree.dynamic.DynamicValue;

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
public class CommaExpressionDynamicValue implements DynamicValue {

    private final List<DynamicValue> paramsList;

    public CommaExpressionDynamicValue(final List<DynamicValue> paramsList) {
        this.paramsList = paramsList;
    }

    public static CommaExpressionDynamicValue from(final List<DynamicValue> expressions) {
        return new CommaExpressionDynamicValue(expressions);
    }

    @Override
    public List<Object> interpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
        return CollectionUtils.isEmpty(childNodeValueList) ? Collections.emptyList() : childNodeValueList;
    }

    @Override
    public List<Node> getChildNodes() {
        return new ArrayList<>(this.paramsList);
    }

    @Override
    public int getNodeCount() {
        return this.paramsList.size();
    }

    public List<DynamicValue> getCommaExpressionValue() {
        return this.paramsList;
    }
}