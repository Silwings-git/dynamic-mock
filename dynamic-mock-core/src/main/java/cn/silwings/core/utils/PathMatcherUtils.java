package cn.silwings.core.utils;

import org.springframework.util.AntPathMatcher;

import java.util.Map;

/**
 * @ClassName PathMacther
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 18:01
 * @Since
 **/
public class PathMatcherUtils {

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    private PathMatcherUtils() {
        throw new AssertionError();
    }

    public static boolean match(final String pattern, final String path) {
        return MATCHER.match(pattern, path);
    }

    public static AntPathMatcher matcher() {
        return MATCHER;
    }

    public static Map<String, String> extractUriTemplateVariables(final String pattern, final String path) {
        return MATCHER.extractUriTemplateVariables(pattern, path);
    }

    public static String fillPatternUrl(final String pattern, final Map<String, String> uriTemplateVariables) {

        String url = pattern;

        for (final Map.Entry<String, String> entry : uriTemplateVariables.entrySet()) {
            url = url.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return url;
    }

}