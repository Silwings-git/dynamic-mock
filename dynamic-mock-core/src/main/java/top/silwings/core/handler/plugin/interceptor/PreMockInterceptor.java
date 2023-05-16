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
public interface PreMockInterceptor {
    // TODO_Silwings: 2023/5/16 做异常中断处理
    void execute(FinalRequestContext requestContext, MockWorkflowControl mockWorkflowControl);

}