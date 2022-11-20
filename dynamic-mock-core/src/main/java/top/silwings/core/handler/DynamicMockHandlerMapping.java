package top.silwings.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
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

    private final MockHandlerPoint mockHandlerPoint;

    private final Method mockMethod;

    public DynamicMockHandlerMapping(final MockHandlerManager mockHandlerManager, final MockHandlerPoint mockHandlerPoint) {
        this.mockHandlerManager = mockHandlerManager;
        this.mockHandlerPoint = mockHandlerPoint;
        try {
            this.mockMethod = MockHandlerPoint.class.getMethod("executeMock", HttpServletRequest.class);
        } catch (NoSuchMethodException e) {
            throw new DynamicMockException(e);
        }
    }

    @Override
    protected HandlerMethod getHandlerInternal(final HttpServletRequest request) throws Exception {

        final HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());

        final String requestUri = this.getUrlPathHelper().getLookupPathForRequest(request);

        if (this.mockHandlerManager.match(requestUri, httpMethod)) {
            return new HandlerMethod(this.mockHandlerPoint, this.mockMethod);
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
