package cn.silwings.core.interpreter.dynamic_expression.function;

import cn.silwings.core.exceptions.DynamicMockException;
import cn.silwings.core.exceptions.TypeCastException;
import cn.silwings.core.handler.context.MockHandlerContext;
import cn.silwings.core.interpreter.ExpressionTreeNode;
import cn.silwings.core.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FunctionDynamicValue
 * @Description 函数
 * @Author Silwings
 * @Date 2022/11/7 20:52
 * @Since
 **/
public abstract class AbstractFunctionExpression implements FunctionExpression {

    private final List<ExpressionTreeNode> functionExpressionList;

    protected AbstractFunctionExpression(final List<ExpressionTreeNode> functionExpressionList) {
        this.functionExpressionList = functionExpressionList;
    }

    /**
     * 获取子节点集
     *
     * @return 子节点集合
     */
    @Override
    public List<ExpressionTreeNode> getChildNodes() {
        return new ArrayList<>(this.functionExpressionList);
    }

    /**
     * 获取函数参数总数
     */
    @Override
    public int getNodeCount() {
        return this.functionExpressionList.size();
    }

    /**
     * 解释动态值(Dynamic Value)实例.即通过上下文和参数计算动态结果值.
     *
     * @param mockHandlerContext 上下文信息
     * @param childNodeValueList 参数信息
     */
    @Override
    public Object interpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
        try {
            return this.doInterpret(mockHandlerContext, childNodeValueList);
        } catch (TypeCastException e) {
            throw new DynamicMockException("Function Expression '" + symbol() + "' execution failed. " + e.getMessage() + ": " + JsonUtils.toJSONString(childNodeValueList), e);
        }
    }

    /**
     * 执行解释
     */
    protected abstract Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList);

    /**
     * 返回该函数的符号
     */
    protected abstract String symbol();
}