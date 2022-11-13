package top.silwings.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName ConvertUtils
 * @Description 转换类
 * @Author Silwings
 * @Date 2022/11/10 22:48
 * @Since
 **/
public class ConvertUtils {

    private ConvertUtils() {
        throw new AssertionError();
    }

    public static <T> T getNoNullOrDefault(final T t, final T defaultValue) {
        return null == t ? defaultValue : t;
    }

    public static String getNoBlankOrDefault(final String value, final String defaultValue) {
        return StringUtils.isBlank(value) ? defaultValue : value;
    }

}