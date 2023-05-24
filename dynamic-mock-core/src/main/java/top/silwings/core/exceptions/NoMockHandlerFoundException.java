package top.silwings.core.exceptions;

import top.silwings.core.handler.context.RequestInfo;

/**
 * @ClassName NoMockHandlerFoundException
 * @Description
 * @Author Silwings
 * @Date 2022/11/10 22:27
 * @Since
 **/
public class NoMockHandlerFoundException extends BaseDynamicMockException {
    public NoMockHandlerFoundException(final RequestInfo requestInfo) {
        super("No mock handler found for " + requestInfo.getHttpMethod() + " " + requestInfo.getRequestUri());
    }
}