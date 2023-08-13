package top.silwings.core.model;

import lombok.Builder;
import lombok.Getter;
import top.silwings.core.common.EnableStatus;

import java.util.Map;

/**
 * @ClassName MockHandlerPluginInfoParam
 * @Description 插件参数信息
 * @Author Silwings
 * @Date 2023/8/13 12:22
 * @Since
 **/
@Getter
@Builder
public class MockHandlerPluginInfoDto {

    // 插件编码
    private String pluginCode;

    // 启用状态
    private EnableStatus enableStatus;

    // 插件参数
    private Map<String, ?> pluginParam;

}