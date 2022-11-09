package top.silwings.core.exceptions;

/**
 * @ClassName NodeAnalyzeException
 * @Description
 * @Author Silwings
 * @Date 2022/11/6 15:09
 * @Since
 **/
public class NodeParseException extends DynamicMockException {
    public NodeParseException() {
        super();
    }

    public NodeParseException(final String message) {
        super(message);
    }

    public NodeParseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NodeParseException(final Throwable cause) {
        super(cause);
    }

    protected NodeParseException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}