package top.silwings.core.handler.plugin.executors;

import org.springframework.core.Ordered;
import top.silwings.core.handler.context.MockPluginContext;
import top.silwings.core.handler.plugin.PluginInterfaceType;

import java.io.Closeable;

/**
 * @ClassName PluginExecutor
 * @Description 插件处理器.通常情况下插件实现应该是无状态的.
 * @Author Silwings
 * @Date 2023/5/29 20:03
 * @Since
 **/
public interface PluginExecutor<T> extends Ordered, Closeable {

    PluginInterfaceType getPluginInterfaceType();

    T execute(MockPluginContext mockPluginContext);

}