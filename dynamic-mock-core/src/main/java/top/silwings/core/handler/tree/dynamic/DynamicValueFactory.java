package top.silwings.core.handler.tree.dynamic;

import org.springframework.stereotype.Component;
import top.silwings.core.handler.tree.dynamic.expression.ExpressionDynamicValueFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionDynamicValueFactory;

/**
 * @ClassName DynamicValueFactory
 * @Description 动态数据工厂
 * @Author Silwings
 * @Date 2022/11/7 21:56
 * @Since
 **/
@Component
public class DynamicValueFactory {

    private final ExpressionDynamicValueFactory expressionFactory;
    private final FunctionDynamicValueFactory functionFactory;

    public DynamicValueFactory(final ExpressionDynamicValueFactory expressionFactory, final FunctionDynamicValueFactory functionFactory) {
        this.expressionFactory = expressionFactory;
        this.functionFactory = functionFactory;
    }

    public static boolean isDynamic(final String str) {
        // 仅以${开头,}结尾的视为动态表达式
        return str.startsWith("${") && str.endsWith("}");
    }

    public DynamicValue buildDynamicValue(final String expression) {

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