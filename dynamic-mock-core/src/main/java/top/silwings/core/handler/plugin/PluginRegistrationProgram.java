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

    PluginExecutor<?> newPluginExecutor(MockHandlerPluginInfo mockHandlerPluginInfo, MockHandlerDto definition);

    MockPluginInfo getMockPluginInfo();

    default void register(MockHandlerPluginInfo mockHandlerPluginInfo, MockHandlerDto definition, PluginExecutorManager manager) {
        manager.register(this.newPluginExecutor(mockHandlerPluginInfo, definition));
    }

}