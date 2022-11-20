package top.silwings.core.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
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
     * @param exceptionSupplier 不满足时要抛出的异常
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
     * @param exceptionSupplier 不一致时要抛出的异常
     */
    public static void hasEqualsSize(final Collection<?> collection, final int size, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(hasEqualsSize(collection, size), exceptionSupplier);
    }

    /**
     * 检查bool是否为true,不为true时抛出异常
     *
     * @param bool              待检查布尔值
     * @param exceptionSupplier 要抛出的异常
     */
    public static void isTrue(final boolean bool, final Supplier<RuntimeException> exceptionSupplier) {
        if (!bool) {
            throw exceptionSupplier.get();
        }
    }

    public static void isNotBlank(final String str, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(StringUtils.isNotBlank(str), exceptionSupplier);
    }

    public static void isNotNull(final Object obj, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(null != obj, exceptionSupplier);
    }

    public static <T> void isEquals(final T argA, final T argB, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(argA.equals(argB), exceptionSupplier);
    }

    public static <T> void isNotEquals(final T argA, final T argB, final Supplier<RuntimeException> exceptionSupplier) {
        isTrue(!argA.equals(argB), exceptionSupplier);
    }
}