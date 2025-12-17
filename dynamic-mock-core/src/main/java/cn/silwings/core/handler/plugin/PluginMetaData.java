package cn.silwings.core.handler.plugin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @ClassName PluginMetaData
 * @Description
 * @Author Silwings
 * @Date 2023/8/14 18:59
 * @Since
 **/
@Getter
@Builder
@AllArgsConstructor
public class PluginMetaData {

    /**
     * 字段规则
     */
    private final List<PluginParamRule> pluginParamRuleList;

}