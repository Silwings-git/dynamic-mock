package top.silwings.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import top.silwings.core.common.Identity;
import top.silwings.core.exceptions.NoMockHandlerFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        log.info("Mock Handler {} registered.", mockHandler.getName());
    }

    public void unregisterHandler(final Identity handlerId) {
        this.handlerMap.remove(handlerId);
    }

    private MockHandler filter(final RequestInfo requestInfo) {

        for (final MockHandler mockHandler : this.handlerMap.values()) {
            if (mockHandler.support(requestInfo)) {
                return mockHandler;
            }
        }

        throw new NoMockHandlerFoundException(requestInfo);
    }

    public ResponseEntity<Object> mock(final Context context) {

        final MockHandler mockHandler = this.filter(RequestInfo.from(context));

        return mockHandler.delay().mock(context);
    }

}