package top.silwings.core.exceptions;

/**
 * @ClassName ExpressionException
 * @Description
 * @Author Silwings
 * @Date 2022/11/6 15:07
 * @Since
 **/
public class ExpressionException extends DynamicMockException {
    public ExpressionException() {
        super();
    }

    public ExpressionException(final String message) {
        super(message);
    }

    public ExpressionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExpressionException(final Throwable cause) {
        super(cause);
    }

    protected ExpressionException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}