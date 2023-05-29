package top.silwings.core.handler.context;

import lombok.Getter;
import top.silwings.core.handler.MockWorkflowControl;
import top.silwings.core.handler.response.MockResponse;

import java.util.Map;

/**
 * @ClassName PreResponseContext
 * @Description 对RequestContext的不可变封装.
 * @Author Silwings
 * @Date 2023/5/16 21:53
 * @Since
 **/
@Getter
public class DefaultPreResponseContext extends DefaultPreMockContext {

    private final MockResponse response;

    public DefaultPreResponseContext(final RequestContext.RequestInfo requestInfo,
                                     final Map<Object, Object> localCache,
                                     final Map<String, Object> customizeSpace,
                                     final MockWorkflowControl mockWorkflowControl,
                                     final MockResponse response) {
        super(requestInfo, localCache, customizeSpace, mockWorkflowControl);
        this.response = response;
    }

    public static DefaultPreResponseContext of(final RequestContext requestContext, final MockResponse response, final MockWorkflowControl control) {
        return new DefaultPreResponseContext(requestContext.getRequestInfo(), requestContext.getLocalCache(), requestContext.getCustomizeSpace(), control, response);
    }

    public static DefaultPreResponseContext of(final DefaultPreMockContext defaultPreMockContext, final MockResponse response) {
        return new DefaultPreResponseContext(defaultPreMockContext.getRequestInfo(), defaultPreMockContext.getLocalCache(), defaultPreMockContext.getCustomizeSpace(), defaultPreMockContext.getMockWorkflowControl(), response);
    }

}