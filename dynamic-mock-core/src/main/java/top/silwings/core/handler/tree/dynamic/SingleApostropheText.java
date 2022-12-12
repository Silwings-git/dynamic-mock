package top.silwings.core.handler.tree.dynamic;

import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.utils.CheckUtils;

/**
 * @ClassName DmString
 * @Description 单引号信息.
 * @Author Silwings
 * @Date 2022/12/8 21:09
 * @Since
 **/
public class SingleApostropheText {

    private final String text;

    public SingleApostropheText(final String text) {
        this.text = text;
    }

    public static boolean isDoubleQuoteString(final String text) {
        try {
            SingleApostropheText.build(text.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static SingleApostropheText build(final String expression) {

        // 仅以单个'开头和结尾,不携带转义符号
        // 如果字符串中间出现了未带奇数个\的',表达式非法

        final char[] chars = expression.trim().toCharArray();

        for (int i = 0; i < chars.length; i++) {

            final char c = chars[i];

            if (i == 0) {
                CheckUtils.isTrue(c == '\'', DynamicMockException.supplier("Invalid expression: " + expression));
            } else if (i == chars.length - 1) {
                CheckUtils.isTrue(c == '\'' && getBackwardSlashNumber(chars, i) == 0, DynamicMockException.supplier("Invalid expression: " + expression));
            } else if (c == '\'') {
                CheckUtils.isTrue(getBackwardSlashNumber(chars, i) % 2 > 0, DynamicMockException.supplier("Invalid expression: " + expression));
            }

        }

        return new SingleApostropheText(expression);
    }

    private static int getBackwardSlashNumber(final char[] chars, final int doubleQuoteIndex) {
        int backwardSlashNum = 0;
        // 检查左边有多少个\,如果\的数量为奇数个,表示不是一个有效的引号
        for (int i = doubleQuoteIndex - 1; i >= 0; i--) {
            if (chars[i] == '\\') {
                backwardSlashNum++;
            } else {
                break;
            }
        }
        return backwardSlashNum;
    }

    public static String tryGetEscapeText(final String text) {
        try {
            return SingleApostropheText.build(text).getEscapeText();
        } catch (Exception e) {
            return text;
        }
    }

    public String getOriginalText() {
        return this.text;
    }

    public String getEscapeText() {

        // 去掉最外层的''
        // 将内部紧跟'的\的数量减少1个

        final String trimText = this.text.trim();
        final String substring = trimText.substring(1, trimText.length() - 1);

        final char[] chars = substring.toCharArray();

        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {

            final char aChar = chars[i];

            if (!(aChar == '\\' && i < chars.length - 1 && chars[i + 1] == '\'')) {
                builder.append(chars[i]);
            }
        }

        return builder.toString();
    }

}