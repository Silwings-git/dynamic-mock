package top.silwings.core.exceptions;

import java.util.function.Supplier;

/**
 * @ClassName DynamicValueCompileException
 * @Description
 * @Author Silwings
 * @Date 2022/11/18 22:38
 * @Since
 **/
public class DynamicValueCompileException extends DynamicMockException {

    public DynamicValueCompileException(final String message) {
        super(message);
    }

    public static DynamicValueCompileException from(final String message) {
        return new DynamicValueCompileException(message);
    }

    public static Supplier<RuntimeException> supplier(final String message) {
        return () -> DynamicValueCompileException.from(message);
    }

}