package top.silwings.core.handler.plugin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @ClassName PluginParamRule
 * @Description
 * @Author Silwings
 * @Date 2023/8/15 11:19
 * @Since
 **/
@Getter
@Builder
@AllArgsConstructor
public class PluginParamRule {

    /**
     * 字段名
     */
    private final String fieldName;

    /**
     * 字段类型
     */
    private final PluginParamFieldType fieldType;

    /**
     * 是否必须
     */
    private final boolean required;

    /**
     * 描述
     */
    private final String description;

}