package cn.silwings.core.handler.context;

import lombok.Getter;
import cn.silwings.core.handler.MockWorkflowControl;
import cn.silwings.core.handler.response.MockResponse;

import java.util.Map;

/**
 * @ClassName PreMockContext
 * @Author Silwings
 * @Date 2023/5/16 21:53
 * @Since
 **/
@Getter
public class DefaultPreMockContext implements MockPluginContext {

    /**
     * HTTP请求信息
     */
    private final RequestContext.RequestInfo requestInfo;

    /**
     * 本地缓存,可用于运行时函数向其中读写数据
     */
    private final Map<Object, Object> localCache;

    /**
     * 自定义空间
     */
    private final Map<String, Object> customizeSpace;

    /**
     * 流程控制类
     */
    private final MockWorkflowControl mockWorkflowControl;

    public DefaultPreMockContext(final RequestContext.RequestInfo requestInfo,
                                 final Map<Object, Object> localCache,
                                 final Map<String, Object> customizeSpace,
                                 final MockWorkflowControl mockWorkflowControl) {
        this.requestInfo = requestInfo;
        this.localCache = localCache;
        this.customizeSpace = customizeSpace;
        this.mockWorkflowControl = mockWorkflowControl;
    }

    public static DefaultPreMockContext of(final RequestContext requestContext, final MockWorkflowControl control) {
        return new DefaultPreMockContext(requestContext.getRequestInfo(), requestContext.getLocalCache(), requestContext.getCustomizeSpace(), control);
    }

    @Override
    public MockResponse getResponse() {
        return null;
    }

}