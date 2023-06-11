package top.silwings.core.handler.plugin;

import top.silwings.core.handler.plugin.executors.PluginExecutor;
import top.silwings.core.handler.plugin.interfaces.Ordered;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PluginManager
 * @Description 插件管理器
 * @Author Silwings
 * @Date 2023/5/29 19:35
 * @Since
 **/
public class PluginExecutorManager {

    private final Map<PluginInterfaceType, List<PluginExecutor<?>>> scriptLanguageScriptMap = new EnumMap<>(PluginInterfaceType.class);

    public List<PluginExecutor<?>> getExecutors(final PluginInterfaceType pluginInterfaceType) {
        return this.scriptLanguageScriptMap.getOrDefault(pluginInterfaceType, Collections.emptyList());
    }

    public void register(final PluginExecutor<?> pluginExecutor) {
        final List<PluginExecutor<?>> pluginExecutorList = this.scriptLanguageScriptMap.computeIfAbsent(pluginExecutor.getPluginInterfaceType(), key -> new ArrayList<>());
        pluginExecutorList.add(pluginExecutor);
        pluginExecutorList.sort(Comparator.comparingInt(Ordered::getOrder));
    }

}