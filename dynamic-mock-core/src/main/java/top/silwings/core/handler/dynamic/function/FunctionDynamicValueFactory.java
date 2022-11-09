package top.silwings.core.handler.dynamic.function;

import lombok.Getter;
import top.silwings.core.handler.Parser;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.DynamicValueFactory;
import top.silwings.core.handler.dynamic.function.functions.IsBlankFunctionFactory;
import top.silwings.core.handler.dynamic.function.functions.SearchFunctionFactory;
import top.silwings.core.handler.dynamic.function.functions.UUIDFunctionFactory;
import top.silwings.core.exceptions.DynamicDataException;

import java.util.ArrayList;
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
public class FunctionDynamicValueFactory {

    private static final FunctionDynamicValueFactory INSTANCE = new FunctionDynamicValueFactory();

    private final MethodExpressionParser methodExpressionParser;

    private final List<FunctionFactory> functionFactoryList;

    private FunctionDynamicValueFactory() {
        this.methodExpressionParser = new MethodExpressionParser();
        this.functionFactoryList = new ArrayList<>();
        this.init();
    }

    private void init() {
        this.functionFactoryList.add(new SearchFunctionFactory());
        this.functionFactoryList.add(new UUIDFunctionFactory());
        this.functionFactoryList.add(new IsBlankFunctionFactory());
    }


    public static FunctionDynamicValueFactory getInstance() {
        return INSTANCE;
    }

    public boolean isFunction(final String expression) {
        return this.methodExpressionParser.support(expression);
    }

    public DynamicValue buildDynamicValue(final String expression, final DynamicValueFactory dynamicValueFactory) {

        final MethodInfo methodInfo = this.methodExpressionParser.parse(expression);

        // 使用名称找到对应函数的工厂,创建方法
        for (final FunctionFactory factory : this.functionFactoryList) {
            if (factory.support(methodInfo.getName())) {
                return factory.buildFunction(dynamicValueFactory.buildDynamicValue(methodInfo.getParamsExpression()));
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