package top.silwings.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import top.silwings.core.common.Identity;
import top.silwings.core.config.MockHandlerHolder;
import top.silwings.core.exceptions.NoMockHandlerFoundException;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.handler.context.RequestInfo;
import top.silwings.core.utils.ConvertUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerManager
 * @Description MockHandler管理器
 * @Author Silwings
 * @Date 2022/11/10 21:49
 * @Since
 **/
@Slf4j
@Component
public class MockHandlerManager {

    private final Map<Identity, MockHandler> handlerMap;

    public MockHandlerManager() {
        this.handlerMap = new ConcurrentHashMap<>();
    }

    public void registerHandler(final MockHandler mockHandler) {
        this.handlerMap.put(mockHandler.getHandlerId(), mockHandler);
        log.info("Mock Handler {}:{} registered.", mockHandler.getHandlerId(), mockHandler.getName());
    }

    public void unregisterHandler(final Identity handlerId) {
        this.handlerMap.remove(handlerId);
        log.info("Mock Handler unregistered, Handler Id: {}", handlerId);
    }

    public boolean match(final String requestUri, final HttpMethod httpMethod) {
        return null != this.filter(RequestInfo.of(requestUri, httpMethod));
    }

    public MockHandler filter(final RequestInfo requestInfo) {

        for (final MockHandler mockHandler : this.handlerMap.values()) {
            if (mockHandler.support(requestInfo)) {
                return mockHandler;
            }
        }

        return null;
    }

    public ResponseEntity<Object> mock(final MockHandlerContext mockHandlerContext) {

        final RequestInfo requestInfo = RequestInfo.from(mockHandlerContext);

        final MockHandler mockHandler = ConvertUtils.getNoNullOrDefault(MockHandlerHolder.get(), () -> this.filter(requestInfo));

        if (null == mockHandler) {
            throw new NoMockHandlerFoundException(requestInfo);
        }

        return mockHandler.mock(mockHandlerContext);
    }

    public Set<Identity> registeredHandlerIds() {
        return this.handlerMap.keySet();
    }

    public Map<Identity, Long> registeredHandlerVersions() {
        return this.handlerMap.values().stream().collect(Collectors.toMap(MockHandler::getHandlerId, MockHandler::getVersion, (v1, v2) -> v2));
    }

}