package top.silwings.core.handler.plugin.interfaces;

import top.silwings.core.handler.context.MockPluginContext;

/**
 * @ClassName PreResponseInterceptor
 * @Description Mock响应前拦截器
 * @Author Silwings
 * @Date 2023/5/16 22:03
 * @Since
 **/
@FunctionalInterface
public interface PreResponseInterceptor {

    void preResponse(MockPluginContext mockPluginContext);

}