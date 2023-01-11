package top.silwings.core.interpreter.expression.operator;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.interpreter.Expression;
import top.silwings.core.interpreter.expression.DynamicExpressionFactory;
import top.silwings.core.utils.JsonUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName OperationDynamicValueFactory
 * @Description 操作符工厂
 * @Author Silwings
 * @Date 2022/11/7 21:55
 * @Since
 **/
@Component
public class OperatorExpressionFactory {

    private final List<OperatorFactory> operatorFactoryList;
    private final List<String> operatorSymbolList;

    public OperatorExpressionFactory(final List<OperatorFactory> operatorFactoryList) {
        this.operatorFactoryList = operatorFactoryList;
        this.operatorSymbolList = operatorFactoryList.stream().map(OperatorFactory::getOperatorSymbol).collect(Collectors.toList());
    }

    public OperatorExpression buildDynamicValue(final List<String> symbolList, final DynamicExpressionFactory dynamicExpressionFactory) {

        // 先将字符串转后缀表达式,再创建对象
        final List<String> suffixList = this.infixToSuffix(symbolList);

        try {
            return this.buildOperationValue(suffixList, dynamicExpressionFactory);
        } catch (Exception e) {
            throw new DynamicMockException("Operator expression parsing failed. symbols: " + JsonUtils.toJSONString(symbolList), e);
        }
    }

    private OperatorExpression buildOperationValue(final List<String> symbolList, final DynamicExpressionFactory dynamicExpressionFactory) {

        // 非操作符,创建value后入队列
        // 操作符,从队列取两个value,new操作实例,入队列
        final LinkedList<Expression> cache = new LinkedList<>();

        for (final String symbol : symbolList) {
            if (this.isOperatorSymbol(symbol)) {

                Expression secondValue = null;
                Expression firstValue = null;
                if (!cache.isEmpty()) {
                    secondValue = cache.removeLast();
                }
                if (!cache.isEmpty()) {
                    firstValue = cache.removeLast();
                }

                cache.add(this.buildOperator(symbol, firstValue, secondValue));

            } else {

                cache.add(dynamicExpressionFactory.buildDynamicValue(symbol));
            }
        }

        if (cache.size() != 1) {
            throw new DynamicMockException("Missing root node");
        }

        if (cache.get(0) instanceof OperatorExpression) {

            return (OperatorExpression) cache.get(0);
        } else {

            throw new DynamicMockException("Missing root node");
        }
    }

    private OperatorExpression buildOperator(final String symbol, final Expression firstValue, final Expression secondValue) {

        final OperatorFactory factory = this.filter(symbol);

        if (null != factory) {
            return factory.buildOperator(Stream.of(firstValue, secondValue).filter(Objects::nonNull).collect(Collectors.toList()));
        }

        throw new DynamicMockException("Operator does not exist: " + symbol);
    }

    private List<String> infixToSuffix(final List<String> symbolList) {

        final Stack<String> stack = new Stack<>();
        final List<String> suffixList = new ArrayList<>();

        for (final String symbol : symbolList) {

            // 如果前一个是操作符,应当将当前符号视为非操作符
            if (this.isOperatorSymbol(symbol)) {

                // 数据操作符时:
                // 1.如果栈是空,直接入栈
                // 2.如果当前操作符优先级大于栈顶操作符,直接入栈
                // 3.如果当前操作符优先级小于等于栈顶操作符,将栈顶元素依次弹栈,加入到suffixList,直到该操作符优先级大于栈顶操作符或栈无数据,将当前操作符入栈

                while (!stack.empty()
                        && this.isOperatorSymbol(stack.peek())
                        && this.getPriority(symbol) <= this.getPriority(stack.peek())) {

                    suffixList.add(stack.pop());
                }

                stack.push(symbol);

            } else {
                suffixList.add(symbol);
            }
        }

        // 遍历完成,弹出栈中剩余元素
        while (!stack.empty()) {
            suffixList.add(stack.pop());
        }

        return suffixList;
    }

    private int getPriority(final String symbol) {
        final OperatorFactory factory = this.filter(symbol);
        if (null == factory) {
            throw new DynamicMockException("Operator does not exist: " + symbol);
        }
        return factory.getPriority();
    }

    private OperatorFactory filter(final String symbol) {
        for (final OperatorFactory factory : this.operatorFactoryList) {
            if (factory.support(symbol)) {
                return factory;
            }
        }
        return null;
    }

    public boolean isOperatorSymbol(final String symbol) {
        return null != this.filter(symbol);
    }

    public List<String> operatorSymbols() {
        return new ArrayList<>(this.operatorSymbolList);
    }

}