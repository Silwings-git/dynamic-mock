package top.silwings.core.handler.tree.dynamic.function;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.Parser;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.handler.tree.dynamic.expression.expressions.CommaExpressionDynamicValue;

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
        for (final FunctionFactory factory : this.functionFactoryList) {
            if (factory.support(methodInfo.getName())) {

                if (StringUtils.isBlank(methodInfo.getParamsExpression())) {
                    return factory.buildFunction(Collections.emptyList());
                }

                final DynamicValue dynamicValue = dynamicValueFactory.buildDynamicValue(methodInfo.getParamsExpression());
                if (dynamicValue instanceof CommaExpressionDynamicValue) {
                    return factory.buildFunction(((CommaExpressionDynamicValue) dynamicValue).getCommaExpressionValue());
                }

                return factory.buildFunction(Collections.singletonList(dynamicValue));
            }
        }

        throw new DynamicMockException("Unsupported function expression: " + expression);
    }

    public static class MethodExpressionParser implements Parser<String, MethodInfo> {

        private static final String REGEX = "^((#)(?<name>\\w+)\\()(?<params>.*)(\\))$";

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

        public MethodInfo parse(final String expression) {
            final Pattern compile = Pattern.compile(REGEX);
            final Matcher matcher = compile.matcher(expression);

            if (matcher.find()) {
                return MethodInfo.of(matcher.group("name"), matcher.group("params"));
            }
            throw new DynamicMockException("Function parsing failed: " + expression);
        }

        public boolean support(final String expression) {
            return Pattern.compile(REGEX).matcher(expression).find() && isSymmetry(expression);
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

        public static MethodInfo of(final String name, final String paramsExpression) {
            return new MethodInfo(name, paramsExpression);
        }
    }

}