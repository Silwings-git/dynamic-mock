package top.silwings.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import top.silwings.core.config.MockHandlerHolder;
import top.silwings.core.exceptions.DynamicMockException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * @ClassName DynamicMockHandlerMapping
 * @Description 自定义处理器映射
 * @Author Silwings
 * @Date 2022/11/20 11:55
 * @Since
 **/
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class DynamicMockHandlerMapping extends AbstractHandlerMethodMapping<Object> {

    private final MockHandlerManager mockHandlerManager;

    private final MockEndPoint mockEndPoint;

    private final Method mockMethod;

    public DynamicMockHandlerMapping(final MockHandlerManager mockHandlerManager, final MockEndPoint mockEndPoint) {
        this.mockHandlerManager = mockHandlerManager;
        this.mockEndPoint = mockEndPoint;
        try {
            this.mockMethod = MockEndPoint.class.getMethod("executeMock", HttpServletRequest.class);
        } catch (NoSuchMethodException e) {
            throw new DynamicMockException(e);
        }
    }

    @Override
    protected HandlerMethod getHandlerInternal(final HttpServletRequest request) throws Exception {

        final HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());

        final String requestUri = this.getUrlPathHelper().getLookupPathForRequest(request);

        final MockHandler mockHandler = this.mockHandlerManager.filter(RequestInfo.of(requestUri, httpMethod));
        if (null != mockHandler) {
            MockHandlerHolder.remove();
            MockHandlerHolder.set(mockHandler);
            return new HandlerMethod(this.mockEndPoint, this.mockMethod);
        }

        return super.getHandlerInternal(request);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    protected boolean isHandler(final Class beanType) {
        return false;
    }

    @Override
    protected Object getMappingForMethod(final Method method, final Class handlerType) {
        return null;
    }

    @Override
    protected Object getMatchingMapping(final Object mapping, final HttpServletRequest request) {
        return null;
    }

    @Override
    protected Comparator<Object> getMappingComparator(final HttpServletRequest request) {
        return null;
    }
}
