package top.silwings.core.handler.plugin.executors;

import top.silwings.core.handler.context.MockPluginContext;
import top.silwings.core.handler.plugin.PluginInterfaceType;
import top.silwings.core.handler.plugin.interfaces.Ordered;
import top.silwings.core.script.ScriptExecutor;

import java.io.Closeable;

/**
 * @ClassName PluginExecutor
 * @Description 插件处理器.通常情况下插件实现应该是无状态的.
 * @Author Silwings
 * @Date 2023/5/29 20:03
 * @Since
 **/
public interface PluginExecutor<T> extends ScriptExecutor<T, MockPluginContext>, Ordered, Closeable {

    PluginInterfaceType getPluginInterfaceType();

}