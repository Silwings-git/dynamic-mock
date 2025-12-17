package cn.silwings.core.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import cn.silwings.core.exceptions.DynamicMockException;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @ClassName CheckUtils
 * @Description
 * @Author Silwings
 * @Date 2022/11/18 22:40
 * @Since
 **/
public class CheckUtils {

    private CheckUtils() {
        throw new AssertionError();
    }

    /**
     * 检查集合的size是否至少满足指定的最小大小
     *
     * @param collection  待检查集合
     * @param minimumSize 最小大小
     * @return true-满足,false-不满足
     */
    public static boolean hasMinimumSize(final Collection<?> collection, final int minimumSize) {
        return CollectionUtils.isNotEmpty(collection) && collection.size() >= minimumSize;
    }

    /**
     * 检查集合的size是否至少满足指定的最小大小.如果不满足将抛出异常
     *
     * @param collection        待检查集合
     * @param minimumSize       最小大小
     * @param exceptionSupplier 异常生成函数
     */
    public static void hasMinimumSize(final Collection<?> collection, final int minimumSize, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(hasMinimumSize(collection, minimumSize), exceptionSupplier);
    }


    /**
     * 检查集合的size是否和指定的size大小一致
     *
     * @param collection 待检查集合
     * @param size       指定大小
     * @return true-一致,false-不一致
     */
    public static boolean hasEqualsSize(final Collection<?> collection, final int size) {
        return CollectionUtils.isNotEmpty(collection) && collection.size() == size;
    }

    /**
     * 检查集合的size是否和指定的size大小一致.如果不满足将抛出异常
     *
     * @param collection        待检查集合
     * @param size              指定大小
     * @param exceptionSupplier 异常生成函数
     */
    public static void hasEqualsSize(final Collection<?> collection, final int size, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(hasEqualsSize(collection, size), exceptionSupplier);
    }

    /**
     * 检查bool是否为true,不为true时抛出异常
     *
     * @param bool              待检查布尔值
     * @param exceptionSupplier 异常生成函数
     */
    public static void isTrue(final boolean bool, final Supplier<RuntimeException> exceptionSupplier) {
        if (!bool) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * 检查字符串是否有内容
     *
     * @param str               待检查字符串
     * @param exceptionSupplier 异常生成函数
     */
    public static void isNotBlank(final String str, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(StringUtils.isNotBlank(str), exceptionSupplier);
    }

    /**
     * 检查obj是否为空
     *
     * @param obj               待检查对象
     * @param exceptionSupplier 异常生成函数
     */
    public static void isNotNull(final Object obj, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(null != obj, exceptionSupplier);
    }

    /**
     * 检查两个对象是否相同
     *
     * @param argA              待检查对象A
     * @param argB              待检查对象B
     * @param exceptionSupplier 异常生成函数
     */
    public static <T> void isEquals(final T argA, final T argB, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(argA.equals(argB), exceptionSupplier);
    }

    /**
     * 检查两个对象是否不相同
     *
     * @param argA              待检查对象A
     * @param argB              待检查对象B
     * @param exceptionSupplier 异常生成函数
     */
    public static <T> void isNotEquals(final T argA, final T argB, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(!argA.equals(argB), exceptionSupplier);
    }

    /**
     * 检查obj是否存在于list中
     *
     * @param obj               待检查对象
     * @param list              待检查集合
     * @param exceptionSupplier 异常生成函数
     */
    public static <T> void isIn(final T obj, final Collection<T> list, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(CollectionUtils.isNotEmpty(list) && list.contains(obj), exceptionSupplier);
    }

    /**
     * 检查集合是否为空
     *
     * @param list              待检查集合
     * @param exceptionSupplier 异常生成函数
     */
    public static void isNotEmpty(final Collection<?> list, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(CollectionUtils.isNotEmpty(list), exceptionSupplier);
    }

    /**
     * 检查字符串str是否具有指定的最小length
     *
     * @param str               待检查字符串
     * @param minLength         最小长度
     * @param exceptionSupplier 异常生成函数
     */
    public static void minLength(final String str, final int minLength, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(StringUtils.isNotBlank(str) && str.length() >= minLength, exceptionSupplier);
    }

    /**
     * 检查字符是否可转换为Integer类型
     *
     * @param str
     * @param exceptionSupplier
     */
    public static void isInteger(final String str, final Supplier<RuntimeException> exceptionSupplier) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * 检查list的size是否介于left和right之间
     *
     * @param list     待检查集合,非空
     * @param left     最小size,必须大于等于0,小于等于right
     * @param right    最大size
     * @param supplier
     */
    public static void sizeBetween(final List<?> list, final int left, final int right, final Supplier<RuntimeException> supplier) {
        isTrue(left >= 0 && right >= 0, DynamicMockException.supplier("The value used for comparison must be greater than or equal to 0."));
        isTrue(left <= right, DynamicMockException.supplier("Left must be less than or equal to right."));
        isTrue(list.size() >= left && list.size() <= right, supplier);
    }

    public static void maxLength(final String arg, final int maxLength, final Supplier<RuntimeException> supplier) {
        isTrue(arg.length() <= maxLength, supplier);
    }
}