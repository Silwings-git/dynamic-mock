package top.silwings.core.interpreter.expression.operator;

import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.TypeCastException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.Expression;
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
public abstract class AbstractOperatorExpression implements OperatorExpression {

    private final List<Expression> functionExpressionList;

    protected AbstractOperatorExpression(final List<Expression> functionExpressionList) {
        this.functionExpressionList = functionExpressionList;
    }

    /**
     * 获取子节点集
     *
     * @return 子节点集合
     */
    @Override
    public List<Expression> getChildNodes() {
        return new ArrayList<>(this.functionExpressionList);
    }

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
            throw new DynamicMockException("Dynamic Expression '" + symbol() + "' execution failed. " + e.getMessage() + ": " + JsonUtils.toJSONString(childNodeValueList), e);
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