package cn.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.silwings.core.handler.plugin.PluginMetaData;

import java.util.List;

/**
 * @ClassName PluginMetaData
 * @Description
 * @Author Silwings
 * @Date 2023/8/14 18:59
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "插件元数据")
public class PluginMetaDataResult {

    /**
     * 字段规则
     */
    @ApiModelProperty(value = "字段规则")
    private List<PluginParamRuleResult> pluginParamRuleList;

    public static PluginMetaDataResult from(final PluginMetaData metadata) {
        final PluginMetaDataResult result = new PluginMetaDataResult();
        if (null != metadata) {
            result.setPluginParamRuleList(PluginParamRuleResult.listFrom(metadata.getPluginParamRuleList()));
        }
        return result;
    }
}