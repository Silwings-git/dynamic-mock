package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import top.silwings.core.handler.plugin.PluginParamFieldType;
import top.silwings.core.handler.plugin.PluginParamRule;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName PluginParamRule
 * @Description
 * @Author Silwings
 * @Date 2023/8/15 11:19
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "字段规则")
public class PluginParamRuleResult {

    @ApiModelProperty(value = "字段名")
    private String fieldName;

    @ApiModelProperty(value = "字段类型")
    private PluginParamFieldType fieldType;

    @ApiModelProperty(value = "是否必须")
    private boolean required;

    @ApiModelProperty(value = "描述")
    private String description;

    public static List<PluginParamRuleResult> listFrom(final List<PluginParamRule> pluginParamRuleList) {
        if (CollectionUtils.isEmpty(pluginParamRuleList)) {
            return Collections.emptyList();
        }
        return pluginParamRuleList
                .stream()
                .map(PluginParamRuleResult::from)
                .collect(Collectors.toList());
    }

    private static PluginParamRuleResult from(final PluginParamRule pluginParamRule) {
        final PluginParamRuleResult result = new PluginParamRuleResult();
        if (null != pluginParamRule) {
            result.setFieldName(pluginParamRule.getFieldName());
            result.setFieldType(pluginParamRule.getFieldType());
            result.setRequired(pluginParamRule.isRequired());
            result.setDescription(pluginParamRule.getDescription());
        }
        return result;
    }
}