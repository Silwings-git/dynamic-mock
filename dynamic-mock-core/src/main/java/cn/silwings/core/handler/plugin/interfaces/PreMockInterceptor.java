package cn.silwings.core.handler.plugin.interfaces;

import cn.silwings.core.handler.context.MockPluginContext;

/**
 * @ClassName PreExecutionInterceptor
 * @Description Mock前拦截器
 * @Author Silwings
 * @Date 2023/5/16 22:01
 * @Since
 **/
@FunctionalInterface
public interface PreMockInterceptor {
    void preMock(MockPluginContext mockPluginContext);

}