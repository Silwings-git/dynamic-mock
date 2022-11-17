package top.silwings.core.handler.tree.dynamic.expression;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.Parser;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.handler.tree.dynamic.expression.expressions.CommaExpressionDynamicValue;
import top.silwings.core.handler.tree.dynamic.expression.expressions.StaticValueExpressionDynamicValue;
import top.silwings.core.handler.tree.dynamic.operator.OperationDynamicValueFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName ExpressionDynamicValueFactory
 * @Description 表达式工厂
 * @Author Silwings
 * @Date 2022/11/7 21:54
 * @Since
 **/
@Component
public class ExpressionDynamicValueFactory {

    private final PrecedenceExpressionParser precedenceExpressionParser;

    private final OperatorExpressionParser operatorExpressionParser;

    private final OperationDynamicValueFactory operationDynamicValueFactory;

    private final KeepOriginalExpressionParser keepOriginalExpressionParser;

    public ExpressionDynamicValueFactory(final OperationDynamicValueFactory operationDynamicValueFactory) {
        this.operationDynamicValueFactory = operationDynamicValueFactory;
        this.precedenceExpressionParser = new PrecedenceExpressionParser();
        this.operatorExpressionParser = new OperatorExpressionParser(operationDynamicValueFactory);
        this.keepOriginalExpressionParser = new KeepOriginalExpressionParser();
    }

    public DynamicValue buildDynamicValue(final String expression, final DynamicValueFactory dynamicValueFactory) {

        // 是否保持原样
        if (this.keepOriginalExpressionParser.support(expression)) {
            return StaticValueExpressionDynamicValue.from(this.keepOriginalExpressionParser.parse(expression));
        }

        final GroupByCommaPriorityResult commaPriorityResult = this.groupByCommaPriority(expression);

        if (commaPriorityResult.hasCommaPriority()) {
            final List<DynamicValue> dynamicValueList = commaPriorityResult.getList().stream().map(dynamicValueFactory::buildDynamicValue).collect(Collectors.toList());
            return CommaExpressionDynamicValue.from(dynamicValueList);
        } else {
            // 判断是否以括号开头结尾
            // 没有逗号的情况下直接判断 expression
            if (this.precedenceExpressionParser.support(expression)) {

                // 有括号优先级的表达式
                final String content = this.precedenceExpressionParser.parse(expression);
                return dynamicValueFactory.buildDynamicValue(content);

            } else {

                // 没有括号优先级的情况下进行拆操作符
                // 拆分表达式
                final List<String> parseList = this.operatorExpressionParser.parse(expression);
                if (parseList.size() == 1) {
                    // 不包含操作符的常量字符
                    return StaticValueExpressionDynamicValue.from(parseList.get(0));
                } else {
                    return this.operationDynamicValueFactory.buildDynamicValue(parseList, dynamicValueFactory);
                }
            }
        }
    }

    @Getter
    public static class OperatorIndex implements Comparable<OperatorIndex> {
        private final String symbol;
        private final int index;

        public OperatorIndex(final String symbol, final int index) {
            this.symbol = symbol;
            this.index = index;
        }

        @Override
        public int compareTo(final OperatorIndex that) {
            return this.index - that.index;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || this.getClass() != other.getClass()) {
                return false;
            }
            final OperatorIndex that = (OperatorIndex) other;
            return this.index == that.index && Objects.equals(this.symbol, that.symbol);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.symbol, this.index);
        }
    }

    private GroupByCommaPriorityResult groupByCommaPriority(final String expression) {

        // 将expression中的方法参数列表以外的逗号去除
        final List<Integer> indexList = new ArrayList<>();

        int num = 0;

        final char[] charArray = expression.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            final char c = charArray[i];
            if ('(' == c || '{' == c || '[' == c) {
                num++;
            } else if (')' == c || '}' == c || ']' == c) {
                num--;
            } else if (',' == c && num == 0) {
                indexList.add(i);
            }
        }

        final StringBuilder builder = new StringBuilder(expression);
        // 逗号和空格长度均为1,所以在替换时可以不考虑角标偏移
        indexList.forEach(index -> builder.replace(index, index + 1, " "));

        return GroupByCommaPriorityResult.of(Arrays.stream(builder.toString().split(" ")).collect(Collectors.toList()), CollectionUtils.isNotEmpty(indexList));
    }


    private static class GroupByCommaPriorityResult {
        private final List<String> list;
        private final boolean commaPriority;

        public GroupByCommaPriorityResult(final List<String> list, final boolean hasCommaPriority) {
            this.list = list;
            this.commaPriority = hasCommaPriority;
        }

        public static GroupByCommaPriorityResult of(final List<String> list, final boolean hasCommaPriority) {
            return new GroupByCommaPriorityResult(list, hasCommaPriority);
        }

        public List<String> getList() {
            return this.list;
        }

        public boolean hasCommaPriority() {
            return this.commaPriority;
        }
    }


    public static class PrecedenceExpressionParser implements Parser<String, String> {

        private static final String REGEX = "^(\\()(?<content>.*)(\\))$";
        private static final Pattern PATTERN = Pattern.compile(REGEX);

        public boolean support(final String expression) {
            return PATTERN.matcher(expression).find();
        }

        public String parse(final String expression) {
            final Matcher matcher = PATTERN.matcher(expression);
            if (matcher.find()) {
                return matcher.group("content");
            } else {
                throw new DynamicMockException("Priority expression parsing failed: " + expression);
            }
        }
    }

    public static class KeepOriginalExpressionParser implements Parser<String, String> {

        private static final String REGEX = "^<(?<content>.*)>$";
        private static final Pattern PATTERN = Pattern.compile(REGEX);

        public boolean support(final String expression) {
            return PATTERN.matcher(expression).find();
        }

        public String parse(final String expression) {
            final Matcher matcher = PATTERN.matcher(expression);
            if (matcher.find()) {
                return matcher.group("content");
            } else {
                throw new DynamicMockException("Failed to parse the original expression: " + expression);
            }
        }
    }

    public static class OperatorExpressionParser implements Parser<String, List<String>> {

        private final OperationDynamicValueFactory operationDynamicValueFactory;

        public OperatorExpressionParser(final OperationDynamicValueFactory operationDynamicValueFactory) {
            this.operationDynamicValueFactory = operationDynamicValueFactory;
        }

        @Override
        public boolean support(final String expression) {
            for (final String operatorSymbol : this.operationDynamicValueFactory.operatorSymbols()) {
                if (expression.contains(operatorSymbol)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<String> parse(final String expression) {
            return this.parse(expression, this.operationDynamicValueFactory.operatorSymbols());
        }

        private List<String> parse(final String str, final List<String> symbolList) {

            if (StringUtils.isBlank(str)) {
                return Collections.singletonList(str);
            }

            // 获取所有操作符所在位置
            final List<OperatorIndex> indexList = new ArrayList<>();
            for (final String symbol : symbolList) {
                int index = 0;

                while (index >= 0) {
                    index = str.indexOf(symbol, index);
                    if (index >= 0) {
                        indexList.add(new OperatorIndex(symbol, index));
                        index += symbol.length();
                    }
                }
            }

            if (CollectionUtils.isEmpty(indexList)) {
                return Collections.singletonList(str);
            }

            indexList.sort(OperatorIndex::compareTo);

            // 按分隔符拆分为集合
            final List<String> result = new ArrayList<>();

            // 优先级入度,只有当前操作符所在入度为0是才允许拆分
            int reentrancy = 0;

            // 下一个操作符索引
            int nextOperatorSymbolIndex = 0;

            // 字符缓存
            StringBuilder cache = new StringBuilder();

            // 下一个字符索引信息
            OperatorIndex nextOperatorSymbol = indexList.get(nextOperatorSymbolIndex++);

            String previousSymbol = null;

            final char[] charArray = str.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                final char c = charArray[i];
                if ('(' == c) {
                    reentrancy++;
                } else if (')' == c) {
                    reentrancy--;
                }
                if (i == nextOperatorSymbol.getIndex()) {
                    if (reentrancy == 0 && i > 0 && StringUtils.isNotBlank(previousSymbol) && !this.operationDynamicValueFactory.isOperatorSymbol(previousSymbol)) {
                        if (cache.length() > 0) {
                            result.add(cache.toString());
                            cache = new StringBuilder();
                        }
                        result.add(nextOperatorSymbol.getSymbol());
                        previousSymbol = nextOperatorSymbol.getSymbol();
                        // 修改i的值,跳过当前操作符
                        i += nextOperatorSymbol.getSymbol().length() - 1;
                    } else {
                        cache.append(c);
                        previousSymbol = String.valueOf(c);
                    }
                    if (nextOperatorSymbolIndex < indexList.size()) {
                        nextOperatorSymbol = indexList.get(nextOperatorSymbolIndex++);
                    }
                } else {
                    cache.append(c);
                    previousSymbol = String.valueOf(c);
                }
            }

            if (cache.length() > 0) {
                result.add(cache.toString());
            }

            return result;
        }
    }

}