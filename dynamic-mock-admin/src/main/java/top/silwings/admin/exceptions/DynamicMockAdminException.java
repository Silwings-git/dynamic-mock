package top.silwings.admin.exceptions;

import lombok.Getter;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @ClassName DynamicMockAdminException
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 13:58
 * @Since
 **/
@Getter
public class DynamicMockAdminException extends RuntimeException {

    private final ErrorCode errorCode;

    private final String[] args;

    public DynamicMockAdminException(final ErrorCode errorCode, final String[] args) {
        super(errorCode.getCode());
        this.errorCode = Objects.requireNonNull(errorCode);
        this.args = args;
    }

    public static Supplier<RuntimeException> supplier(final ErrorCode errorCode) {
        return () -> DynamicMockAdminException.from(errorCode);
    }

    public static Supplier<RuntimeException> supplier(final ErrorCode errorCode, final String arg) {
        return () -> DynamicMockAdminException.of(errorCode, arg);
    }

    public static DynamicMockAdminException from(final ErrorCode errorCode) {
        return new DynamicMockAdminException(errorCode, new String[0]);
    }

    public static DynamicMockAdminException of(final ErrorCode errorCode, final String arg) {
        return new DynamicMockAdminException(errorCode, new String[]{arg});
    }

    public static DynamicMockAdminException of(final ErrorCode errorCode, final String arg1, final String arg2) {
        return new DynamicMockAdminException(errorCode, new String[]{arg1, arg2});
    }

    public static DynamicMockAdminException of(final ErrorCode errorCode, final String arg1, final String arg2, final String arg3) {
        return new DynamicMockAdminException(errorCode, new String[]{arg1, arg2, arg3});
    }

    public static DynamicMockAdminException of(final ErrorCode errorCode, final String arg1, final String arg2, final String arg3, final String arg4) {
        return new DynamicMockAdminException(errorCode, new String[]{arg1, arg2, arg3, arg4});
    }

    public static DynamicMockAdminException of(final ErrorCode errorCode, final String arg1, final String arg2, final String arg3, final String arg4, final String arg5) {
        return new DynamicMockAdminException(errorCode, new String[]{arg1, arg2, arg3, arg4, arg5});
    }

    public static DynamicMockAdminException of(final ErrorCode errorCode, final String[] arg) {
        return new DynamicMockAdminException(errorCode, arg);
    }
}