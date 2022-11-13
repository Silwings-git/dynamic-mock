package top.silwings.core.exceptions;

/**
 * @ClassName DynamicFunctionParamValidateException
 * @Description
 * @Author Silwings
 * @Date 2022/11/13 18:06
 * @Since
 **/
public class DynamicFunctionParamValidateException extends DynamicMockException {
    public DynamicFunctionParamValidateException() {
    }

    public DynamicFunctionParamValidateException(final String message) {
        super(message);
    }

    public DynamicFunctionParamValidateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DynamicFunctionParamValidateException(final Throwable cause) {
        super(cause);
    }

    public DynamicFunctionParamValidateException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}