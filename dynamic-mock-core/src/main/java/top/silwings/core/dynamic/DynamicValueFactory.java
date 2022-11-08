package top.silwings.core.dynamic;

import top.silwings.core.dynamic.expression.ExpressionDynamicValueFactory;
import top.silwings.core.dynamic.function.FunctionDynamicValueFactory;

/**
 * @ClassName DynamicValueFactory
 * @Description 动态数据工厂
 * @Author Silwings
 * @Date 2022/11/7 21:56
 * @Since
 **/
public class DynamicValueFactory {

    private final ExpressionDynamicValueFactory expressionFactory;
    private final FunctionDynamicValueFactory functionFactory;

    public DynamicValueFactory() {
        this.expressionFactory = ExpressionDynamicValueFactory.getInstance();
        this.functionFactory = FunctionDynamicValueFactory.getInstance();
    }

    public DynamicValue buildDynamicValue(final String expression) {

        final String actualExpression = expression.replace(" ", "");

        // 选择第一个工厂
        // 检查是否是单个函数表达式,如果是使用函数工厂,否则使用表达式工厂
        if (this.functionFactory.isFunction(actualExpression)) {
            return this.functionFactory.buildDynamicValue(actualExpression, this);
        } else {
            return this.expressionFactory.buildDynamicValue(actualExpression, this);
        }
    }

}