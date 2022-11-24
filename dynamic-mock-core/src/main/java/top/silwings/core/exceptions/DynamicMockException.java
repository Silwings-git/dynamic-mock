package top.silwings.core.exceptions;

import java.util.function.Supplier;

/**
 * @ClassName EasyMockException
 * @Description
 * @Author Silwings
 * @Date 2022/11/6 15:06
 * @Since
 **/
public class DynamicMockException extends RuntimeException {

    public DynamicMockException() {
        super();
    }

    public DynamicMockException(final String message) {
        super(message);
    }

    public DynamicMockException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DynamicMockException(final Throwable cause) {
        super(cause);
    }

    public static DynamicMockException from(final String message) {
        return new DynamicMockException(message);
    }

    public static Supplier<RuntimeException> supplier(final String message) {
        return () -> DynamicMockException.from(message);
    }

    public static DynamicMockException of(final String message, final Throwable cause) {
        return new DynamicMockException(message, cause);
    }

    public static Supplier<RuntimeException> supplier(final String message, final Throwable cause) {
        return () -> DynamicMockException.of(message, cause);
    }

    public static DynamicMockException from(final Throwable cause) {
        return new DynamicMockException(cause);
    }

    public static Supplier<RuntimeException> supplier(final Throwable cause) {
        return () -> DynamicMockException.from(cause);
    }

}