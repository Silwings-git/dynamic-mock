package top.silwings.core.handler.plugin.interceptor;

import top.silwings.core.handler.MockWorkflowControl;
import top.silwings.core.handler.context.FinalRequestContext;

/**
 * @ClassName PreExecutionInterceptor
 * @Description Mock前拦截器
 * @Author Silwings
 * @Date 2023/5/16 22:01
 * @Since
 **/
@FunctionalInterface
public interface PreMockInterceptor {
    void execute(FinalRequestContext requestContext, MockWorkflowControl mockWorkflowControl);

}