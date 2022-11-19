package top.silwings.admin.exceptions;

/**
 * @ClassName DynamicMockAdminException
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 13:58
 * @Since
 **/
public class DynamicMockAdminException extends RuntimeException {

    public DynamicMockAdminException(final String message) {
        super(message);
    }

    public DynamicMockAdminException(final Throwable cause) {
        super(cause);
    }

    public static DynamicMockAdminException from(final String message) {
        return new DynamicMockAdminException(message);
    }
}