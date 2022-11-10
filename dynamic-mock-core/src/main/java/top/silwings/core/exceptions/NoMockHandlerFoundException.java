package top.silwings.core.exceptions;

import top.silwings.core.handler.RequestInfo;

/**
 * @ClassName NoMockHandlerFoundException
 * @Description
 * @Author Silwings
 * @Date 2022/11/10 22:27
 * @Since
 **/
public class NoMockHandlerFoundException extends DynamicMockException {

    public NoMockHandlerFoundException() {
    }

    public NoMockHandlerFoundException(final String message) {
        super(message);
    }

    public NoMockHandlerFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NoMockHandlerFoundException(final Throwable cause) {
        super(cause);
    }

    public NoMockHandlerFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NoMockHandlerFoundException(final RequestInfo requestInfo) {
        super(requestInfo.getHttpMethod() + ":" + requestInfo.getRequestUri());
    }
}