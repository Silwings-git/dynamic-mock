package top.silwings.core.handler.plugin.interceptor;

import top.silwings.core.handler.MockWorkflowControl;
import top.silwings.core.handler.context.FinalRequestContext;
import top.silwings.core.handler.response.MockResponse;

/**
 * @ClassName PreResponseInterceptor
 * @Description Mock响应前拦截器
 * @Author Silwings
 * @Date 2023/5/16 22:03
 * @Since
 **/
@FunctionalInterface
public interface PreResponseInterceptor {

    void execute(MockResponse response, FinalRequestContext requestContext, MockWorkflowControl mockWorkflowControl);

}