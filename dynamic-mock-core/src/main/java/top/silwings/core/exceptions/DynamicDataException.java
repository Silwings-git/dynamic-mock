package top.silwings.core.exceptions;

/**
 * @ClassName DynamicDataException
 * @Description
 * @Author Silwings
 * @Date 2022/11/6 23:27
 * @Since
 **/
public class DynamicDataException extends DynamicMockException {
    public DynamicDataException() {
        // TODO_Silwings: 2022/11/12 整理错误描述 
        super();
    }

    public DynamicDataException(final String message) {
        super(message);
    }

    public DynamicDataException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DynamicDataException(final Throwable cause) {
        super(cause);
    }

    protected DynamicDataException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}