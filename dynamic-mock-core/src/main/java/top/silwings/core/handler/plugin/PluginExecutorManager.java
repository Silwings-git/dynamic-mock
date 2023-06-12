package top.silwings.core.handler.plugin;

import lombok.extern.slf4j.Slf4j;
import top.silwings.core.handler.plugin.executors.PluginExecutor;
import top.silwings.core.handler.plugin.interfaces.Ordered;

import java.io.Closeable;
import java.io.IOException;
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
@Slf4j
public class PluginExecutorManager implements Closeable {

    private final Map<PluginInterfaceType, List<PluginExecutor<?>>> scriptLanguageScriptMap = new EnumMap<>(PluginInterfaceType.class);

    public List<PluginExecutor<?>> getExecutors(final PluginInterfaceType pluginInterfaceType) {
        return this.scriptLanguageScriptMap.getOrDefault(pluginInterfaceType, Collections.emptyList());
    }

    public void register(final PluginExecutor<?> pluginExecutor) {
        final List<PluginExecutor<?>> pluginExecutorList = this.scriptLanguageScriptMap.computeIfAbsent(pluginExecutor.getPluginInterfaceType(), key -> new ArrayList<>());
        pluginExecutorList.add(pluginExecutor);
        pluginExecutorList.sort(Comparator.comparingInt(Ordered::getOrder));
    }

    @Override
    public void close() {
        this.scriptLanguageScriptMap.values().forEach(pluginExecutors -> pluginExecutors.forEach(pluginExecutor -> {
            try {
                pluginExecutor.close();
            } catch (IOException e) {
                log.error("Plug-in shutdown exception.", e);
            }
        }));
    }
}