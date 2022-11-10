package top.silwings.core.handler.dynamic.operation;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.DynamicValueFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
public class OperationDynamicValueFactory {

    public DynamicValue buildDynamicValue(final List<String> symbolList, final DynamicValueFactory dynamicValueFactory) {

        // 先将字符串转后缀表达式,再创建对象
        final List<String> suffixList = this.infixToSuffix(symbolList);

        return this.buildOperationValue(suffixList, dynamicValueFactory);
    }

    private DynamicValue buildOperationValue(final List<String> symbolList, final DynamicValueFactory dynamicValueFactory) {

        // 非操作符,创建value后入队列
        // 操作符,从队列取两个value,new操作实例,入队列
        final LinkedList<DynamicValue> cache = new LinkedList<>();

        for (final String symbol : symbolList) {
            if (ExtendedOperation.isOperation(symbol)) {

                final ExtendedOperation operator = ExtendedOperation.valueOfSymbol(symbol);

                final DynamicValue secondValue = cache.removeLast();
                final DynamicValue firstValue = cache.removeLast();

                cache.add(operator.getOperatorConstructor().apply(Stream.of(firstValue, secondValue).collect(Collectors.toList())));

            } else {

                cache.add(dynamicValueFactory.buildDynamicValue(symbol));
            }
        }

        if (cache.size() != 1) {
            throw new DynamicDataException("无根节点");
        }

        return cache.get(0);
    }

    private List<String> infixToSuffix(final List<String> symbolList) {

        final Stack<String> stack = new Stack<>();
        final List<String> suffixList = new ArrayList<>();

        for (final String symbol : symbolList) {

            // 如果前一个是操作符,应当将当前符号视为非操作符
            if (ExtendedOperation.isOperation(symbol)) {

                // 数据操作符时:
                // 1.如果栈是空,直接入栈
                // 2.如果当前操作符优先级大于栈顶操作符,直接入栈
                // 3.如果当前操作符优先级小于等于栈顶操作符,将栈顶元素依次弹栈,加入到suffixList,直到该操作符优先级大于栈顶操作符或栈无数据,将当前操作符入栈

                while (!stack.empty()
                        && ExtendedOperation.isOperation(stack.peek())
                        && ExtendedOperation.getPriority(symbol) <= ExtendedOperation.getPriority(stack.peek())) {

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

}