package cn.silwings.core.interpreter.dynamic_expression;

import org.springframework.stereotype.Component;
import cn.silwings.core.interpreter.ExpressionTreeNode;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionExpressionFactory;

/**
 * @ClassName DynamicValueFactory
 * @Description 动态数据工厂
 * @Author Silwings
 * @Date 2022/11/7 21:56
 * @Since
 **/
@Component
public class DynamicExpressionFactory {

    private final ExpressionFactory expressionFactory;
    private final FunctionExpressionFactory functionFactory;

    public DynamicExpressionFactory(final ExpressionFactory expressionFactory, final FunctionExpressionFactory functionFactory) {
        this.expressionFactory = expressionFactory;
        this.functionFactory = functionFactory;
    }

    public static boolean isDynamic(final String str) {
        // 仅以${开头,}结尾的视为动态表达式
        return str.startsWith("${") && str.endsWith("}");
    }

    public ExpressionTreeNode buildDynamicValue(final String expression) {

        String actualExpression = expression.trim();

        if (isDynamic(actualExpression)) {

            actualExpression = actualExpression.substring(2, actualExpression.length() - 1);
        }

        // 选择第一个工厂
        // 检查是否是单个函数表达式,如果是使用函数工厂,否则使用表达式工厂
        if (this.functionFactory.isFunction(actualExpression)) {
            return this.functionFactory.buildDynamicValue(actualExpression, this);
        } else {
            return this.expressionFactory.buildDynamicValue(actualExpression, this);
        }
    }

}