package top.silwings.core.exceptions;

/**
 * @ClassName EasyMockException
 * @Description
 * @Author Silwings
 * @Date 2022/11/6 15:06
 * @Since
 **/
public class EasyMockException extends RuntimeException {

    public EasyMockException() {
        super();
    }

    public EasyMockException(final String message) {
        super(message);
    }

    public EasyMockException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EasyMockException(final Throwable cause) {
        super(cause);
    }

    protected EasyMockException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}