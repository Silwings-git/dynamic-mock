package top.silwings.core.handler.plugin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.silwings.core.common.EnableStatus;

import java.util.Map;

/**
 * @ClassName MockHandlerPluginInfoDto
 * @Description 插件参数信息
 * @Author Silwings
 * @Date 2023/8/13 12:22
 * @Since
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MockHandlerPluginInfo {

    // 插件编码
    private String pluginCode;

    // 启用状态
    private EnableStatus enableStatus;

    // 插件参数
    private Map<String, ?> pluginParam;

}