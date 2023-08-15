package top.silwings.core.handler.plugin;

import lombok.Builder;
import lombok.Getter;

/**
 * @ClassName MockPluginInfo
 * @Description
 * @Author Silwings
 * @Date 2023/8/14 17:03
 * @Since
 **/
@Getter
@Builder
public class MockPluginInfo {

    /**
     * 插件编码
     */
    private String pluginCode;

    /**
     * 插件名称
     */
    private String pluginName;

    /**
     * 描述
     */
    private String description;

    /**
     * 元数据
     */
    private PluginMetaData metadata;

    public static MockPluginInfo copyOf(final MockPluginInfo mockPluginInfo) {
        return MockPluginInfo.builder()
                .pluginCode(mockPluginInfo.getPluginCode())
                .pluginName(mockPluginInfo.getPluginName())
                .description(mockPluginInfo.getDescription())
                .metadata(mockPluginInfo.getMetadata())
                .build();
    }

}