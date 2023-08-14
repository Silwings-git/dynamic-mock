package top.silwings.core.handler.plugin;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName MockPluginInfo
 * @Description
 * @Author Silwings
 * @Date 2023/8/14 17:03
 * @Since
 **/
@Getter
@Setter
public class MockPluginInfo<T> {

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
    private T metadata;

    public static <E> MockPluginInfo<E> copyOf(final MockPluginInfo<E> mockPluginInfo) {
        final MockPluginInfo<E> res = new MockPluginInfo<>();
        res.setPluginCode(mockPluginInfo.getPluginCode());
        res.setPluginName(mockPluginInfo.getPluginName());
        res.setDescription(mockPluginInfo.getDescription());
        res.setMetadata(mockPluginInfo.getMetadata());
        return res;
    }
}