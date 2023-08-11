package top.silwings.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName MessageFormatUtils
 * @Description
 * @Author Silwings
 * @Date 2023/8/11 18:47
 * @Since
 **/
public class MessageFormatUtils {

    private static final Pattern REGEX_PATTERN = Pattern.compile("\\{(\\d+)}");

    public static String format(final String pattern, final Object... arguments) {

        final Matcher matcher = REGEX_PATTERN.matcher(pattern);

        final StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            final int index = Integer.parseInt(matcher.group(1));
            final String replacement = (index < arguments.length) ? String.valueOf(arguments[index]) : matcher.group();
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

}