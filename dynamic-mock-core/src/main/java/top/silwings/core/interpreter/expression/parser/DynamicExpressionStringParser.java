package top.silwings.core.interpreter.expression.parser;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.interpreter.Parser;

import java.util.Stack;

/**
 * @ClassName DynamicExpressionString
 * @Description 动态表达式字符串
 * @Author Silwings
 * @Date 2023/1/4 10:30
 * @Since
 **/
@Component
public class DynamicExpressionStringParser implements Parser<String, String> {

    private static final char ESCAPE_SYMBOL = '^';
    private static final char STRING_SYMBOL = '\'';

    private static char getCharacterValue(final char c) {
        return c == '\'' || c == '^' ? '\0' : c;
    }

    @Override
    public boolean support(final String text) {
        return this.doParse(text, new SupportParserResult()).support();
    }

    @Override
    public String parse(final String text) {
        return this.doParse(text, new ParseParserResult(text.length())).getResult();
    }

    private ParserResult doParse(final String text, final ParserResult parserResult) {

        parserResult.setOriginalText(text);

        if (StringUtils.isBlank(text)) {
            return parserResult.fail(DynamicValueCompileException.from("Is empty !"));
        }

        // 首先执行基本检查
        final String trim = text.trim();
        if (trim.charAt(0) != STRING_SYMBOL || trim.charAt(trim.length() - 1) != STRING_SYMBOL) {
            return parserResult.fail(DynamicValueCompileException.from("Start and end identification error: " + text));
        }

        final char[] chars = text.toCharArray();

        final Stack<Character> specialSymbolsStack = new Stack<>();

        // 遇到转义符号时入栈,否则直接获取字符值入堆
        for (int i = 0; i < chars.length; i++) {

            final char c = chars[i];

            if (!specialSymbolsStack.isEmpty() && ESCAPE_SYMBOL == specialSymbolsStack.peek()) {

                specialSymbolsStack.pop();
                parserResult.setValue(i, c);

            } else if (STRING_SYMBOL == c) {

                // 如果栈为空,入栈
                // 如果栈不为空且栈顶不是转义符,检查是否是最后一个元素
                if (specialSymbolsStack.isEmpty()) {
                    specialSymbolsStack.push(c);
                } else if (i != chars.length - 1) {
                    return parserResult.fail(DynamicValueCompileException.from("Illegal format : " + text));
                }

            } else if (ESCAPE_SYMBOL == c) {

                specialSymbolsStack.push(c);

            } else {

                parserResult.setValue(i, getCharacterValue(c));
            }
        }

        return parserResult.ok();
    }

    public interface ParserResult {

        ParserResult fail(RuntimeException exception);

        ParserResult setValue(int index, char value);

        ParserResult ok();

        boolean support();

        String getResult();

        String getOriginalText();

        ParserResult setOriginalText(String originalText);

        RuntimeException getException();

    }

    private static class SupportParserResult implements ParserResult {

        private boolean success = false;

        private String originalText;

        private RuntimeException exception;

        @Override
        public ParserResult fail(final RuntimeException exception) {
            this.success = false;
            this.exception = exception;
            return this;
        }

        @Override
        public ParserResult setValue(final int index, final char value) {
            return this;
        }

        @Override
        public ParserResult setOriginalText(final String originalText) {
            this.originalText = originalText;
            return this;
        }

        @Override
        public ParserResult ok() {
            this.success = true;
            return this;
        }

        @Override
        public boolean support() {
            return this.success;
        }

        @Override
        public String getResult() {
            throw new IllegalStateException();
        }

        @Override
        public String getOriginalText() {
            return this.originalText;
        }

        @Override
        public RuntimeException getException() {
            return this.exception;
        }
    }

    private static class ParseParserResult implements ParserResult {

        private final char[] outArray;
        private boolean success = false;
        private String result;

        private String originalText;

        private RuntimeException exception;

        public ParseParserResult(final int outArrayLength) {
            this.outArray = new char[outArrayLength];
        }

        @Override
        public ParserResult fail(final RuntimeException exception) {
            this.exception = exception;
            this.success = false;
            throw exception;
        }

        @Override
        public ParserResult setValue(final int index, final char value) {
            outArray[index] = value;
            return this;
        }

        @Override
        public ParserResult setOriginalText(final String originalText) {
            this.originalText = originalText;
            return this;
        }

        @Override
        public ParserResult ok() {
            this.success = true;
            return this;
        }

        @Override
        public boolean support() {
            return this.success;
        }

        @Override
        public String getResult() {

            if (null == this.result) {
                final StringBuilder sb = new StringBuilder();

                for (final char c : outArray) {
                    if (c != '\0') {
                        sb.append(c);
                    }
                }

                this.result = sb.toString();
            }

            return this.result;
        }

        @Override
        public String getOriginalText() {
            return this.originalText;
        }

        @Override
        public RuntimeException getException() {
            return this.exception;
        }

    }

}