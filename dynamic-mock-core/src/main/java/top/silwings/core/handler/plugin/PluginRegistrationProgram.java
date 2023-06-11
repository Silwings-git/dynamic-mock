package top.silwings.core.handler.plugin;

import top.silwings.core.model.MockHandlerDto;

/**
 * @ClassName ScriptRegistrationProgram
 * @Description 脚本注册程序
 * @Author Silwings
 * @Date 2023/6/11 20:02
 * @Since
 **/
public interface PluginRegistrationProgram {
    void register(final MockHandlerDto definition, final PluginExecutorManager manager);
}