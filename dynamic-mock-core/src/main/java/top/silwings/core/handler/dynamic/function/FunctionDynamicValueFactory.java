package top.silwings.core.handler.dynamic.function;

import lombok.Getter;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Parser;
import top.silwings.core.handler.dynamic.DynamicFactory;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.DynamicValueFactory;
import top.silwings.core.handler.dynamic.expression.expressions.CommaExpressionDynamicValue;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName FunctionDynamicValueFactory
 * @Description 函数工厂
 * @Author Silwings
 * @Date 2022/11/7 21:55
 * @Since
 **/
@Component
public class FunctionDynamicValueFactory {

    private final MethodExpressionParser methodExpressionParser;

    private final List<FunctionFactory> functionFactoryList;

    public FunctionDynamicValueFactory(final List<FunctionFactory> functionFactoryList) {
        this.methodExpressionParser = new MethodExpressionParser();
        this.functionFactoryList = functionFactoryList;
    }

    public boolean isFunction(final String expression) {
        return this.methodExpressionParser.support(expression);
    }

    public DynamicValue buildDynamicValue(final String expression, final DynamicValueFactory dynamicValueFactory) {

        final MethodInfo methodInfo = this.methodExpressionParser.parse(expression);

        // 使用名称找到对应函数的工厂,创建方法
        for (final DynamicFactory factory : this.functionFactoryList) {
            if (factory.support(methodInfo.getName())) {
                final DynamicValue dynamicValue = dynamicValueFactory.buildDynamicValue(methodInfo.getParamsExpression());
                if (dynamicValue instanceof CommaExpressionDynamicValue) {
                    return factory.buildFunction(((CommaExpressionDynamicValue) dynamicValue).getCommaExpressionValue());
                }
                return factory.buildFunction(Collections.singletonList(dynamicValue));
            }
        }

        throw new DynamicDataException("函数不存在");
    }

    public static class MethodExpressionParser implements Parser<String, MethodInfo> {

        private static final String REGEX = "^((#)(?<name>\\w+)\\()(?<params>.*)(\\))$";

        public MethodInfo parse(final String expression) {
            final Pattern compile = Pattern.compile(REGEX);
            final Matcher matcher = compile.matcher(expression);

            if (matcher.find()) {
                return new MethodInfo(matcher.group("name"), matcher.group("params"));
            }
            throw new DynamicDataException("方法解析失败");
        }

        public boolean support(final String expression) {
            return Pattern.compile(REGEX).matcher(expression).find() && isSymmetry(expression);
        }

        private static boolean isSymmetry(final String expression) {

            // 优先级为1的右括号应有且仅有一个且位于最后
            int priority = 1;

            final char[] charArray = expression.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                if ('(' == charArray[i]) {
                    priority++;
                } else if (')' == charArray[i]) {
                    priority--;
                    if (priority == 1 && i != charArray.length - 1) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    @Getter
    public static class MethodInfo {
        private final String name;
        private final String paramsExpression;

        public MethodInfo(final String name, final String paramsExpression) {
            this.name = name;
            this.paramsExpression = paramsExpression;
        }

        public static MethodInfo from(final String name, final String paramsExpression) {
            return new MethodInfo(name, paramsExpression);
        }
    }

}