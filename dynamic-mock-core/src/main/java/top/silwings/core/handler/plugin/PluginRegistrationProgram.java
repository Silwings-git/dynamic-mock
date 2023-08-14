package top.silwings.core.handler.plugin;

import top.silwings.core.handler.plugin.executors.PluginExecutor;
import top.silwings.core.model.MockHandlerDto;

/**
 * @ClassName PluginRegistrationProgram
 * @Description 插件注册程序
 * @Author Silwings
 * @Date 2023/6/11 20:02
 * @Since
 **/
public interface PluginRegistrationProgram {

    PluginExecutor<?> newPluginExecutor(String pluginCode, MockHandlerDto definition);

    MockPluginInfo getMockPluginInfo();

    default void register(String pluginCode, MockHandlerDto definition, PluginExecutorManager manager) {
        manager.register(this.newPluginExecutor(pluginCode, definition));
    }

}