package top.silwings.core.exceptions;

/**
 * @ClassName MockTaskException
 * @Description
 * @Author Silwings
 * @Date 2022/11/13 13:24
 * @Since
 **/
public class MockTaskException extends DynamicMockException {

    public MockTaskException() {
    }

    public MockTaskException(final String message) {
        super(message);
    }

    public MockTaskException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MockTaskException(final Throwable cause) {
        super(cause);
    }

    public MockTaskException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}