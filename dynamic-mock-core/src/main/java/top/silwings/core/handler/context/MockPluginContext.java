package top.silwings.core.handler.context;

import top.silwings.core.handler.MockWorkflowControl;
import top.silwings.core.handler.response.MockResponse;

import java.util.Map;

/**
 * @ClassName PreMockContext
 * @Description
 * @Author Silwings
 * @Date 2023/5/29 22:04
 * @Since
 **/
public interface MockPluginContext {

    RequestContext.RequestInfo getRequestInfo();

    Map<Object, Object> getLocalCache();

    Map<String, Object> getCustomizeSpace();

    MockWorkflowControl getMockWorkflowControl();

    MockResponse getResponse();

}