package cn.silwings.core.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

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

    public static <T> T getNoNullOrDefault(final T t, final Supplier<T> defaultValueFunction) {
        return null == t ? defaultValueFunction.get() : t;
    }

    public static <T, R> R getNoNullOrDefault(final T t, final R defaultValue, final Function<T, R> noNullFun) {
        return null == t ? defaultValue : noNullFun.apply(t);
    }

    public static String getNoBlankOrDefault(final String value, final String defaultValue) {
        return StringUtils.isBlank(value) ? defaultValue : value;
    }

    public static <T> T getNoBlankOrDefault(final String value, final T defaultValue, final Function<String, T> noBlankFun) {
        return StringUtils.isBlank(value) ? defaultValue : noBlankFun.apply(value);
    }

    public static <T, R> R getNoEmpty(final List<T> list, final R defaultValue, final Function<List<T>, R> noEmptyFun) {
        return CollectionUtils.isEmpty(list) ? defaultValue : noEmptyFun.apply(list);
    }

}