package top.silwings.core.handler;

import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.NoMockHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MockHandlerManager
 * @Description MockHandler管理器
 * @Author Silwings
 * @Date 2022/11/10 21:49
 * @Since
 **/
@Component
public class MockHandlerManager {

    private final List<MockHandler> mockHandlerList = new ArrayList<>();

    /**
     * 404处理器
     */
    private final MockHandler noHandlerMockHandler = MockHandler.builder().build();

    public MockHandlerManager() {
        this.mockHandlerList.add(MockHandler.builder().build());
    }

    private MockHandler filter(final RequestInfo requestInfo) {

        for (final MockHandler mockHandler : this.mockHandlerList) {
            if (mockHandler.support(requestInfo)) {
                return mockHandler;
            }
        }

        throw new NoMockHandlerFoundException(requestInfo);
    }

    public MockResponse mock(final Context context) {

        final MockHandler mockHandler = this.filter(RequestInfo.from(context));

        return mockHandler.mock(context);
    }

}