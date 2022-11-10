package top.silwings.core.handler.dynamic;

import org.springframework.stereotype.Component;
import top.silwings.core.handler.dynamic.expression.ExpressionDynamicValueFactory;
import top.silwings.core.handler.dynamic.function.FunctionDynamicValueFactory;

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