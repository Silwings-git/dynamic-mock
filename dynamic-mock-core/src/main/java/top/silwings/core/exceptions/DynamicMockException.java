package top.silwings.core.exceptions;

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

    protected DynamicMockException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}