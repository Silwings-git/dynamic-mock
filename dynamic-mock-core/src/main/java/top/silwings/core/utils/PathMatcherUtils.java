package top.silwings.core.utils;

import org.springframework.util.AntPathMatcher;

/**
 * @ClassName PathMacther
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 18:01
 * @Since
 **/
public class PathMatcherUtils {

    private PathMatcherUtils() {
        throw new AssertionError();
    }

    private static final AntPathMatcher matcher = new AntPathMatcher();

    public static boolean match(final String pattern, final String path) {
        return matcher.match(pattern, path);
    }


}