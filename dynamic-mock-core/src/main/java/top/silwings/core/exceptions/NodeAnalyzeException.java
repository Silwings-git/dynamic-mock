package top.silwings.core.exceptions;

/**
 * @ClassName NodeAnalyzeException
 * @Description
 * @Author Silwings
 * @Date 2022/11/6 15:09
 * @Since
 **/
public class NodeAnalyzeException extends EasyMockException {
    public NodeAnalyzeException() {
        super();
    }

    public NodeAnalyzeException(final String message) {
        super(message);
    }

    public NodeAnalyzeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NodeAnalyzeException(final Throwable cause) {
        super(cause);
    }

    protected NodeAnalyzeException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}