package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import top.silwings.core.handler.plugin.MockPluginInfo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockPluginInfo
 * @Description
 * @Author Silwings
 * @Date 2023/8/14 17:03
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "插件信息")
public class MockPluginInfoResult {

    @ApiModelProperty(value = "插件编码", example = "PG001")
    private String pluginCode;

    @ApiModelProperty(value = "插件名称", example = "插件名称")
    private String pluginName;

    @ApiModelProperty(value = "描述", example = "描述")
    private String description;

    @ApiModelProperty(value = "元数据")
    private PluginMetaDataResult metadata;

    public static List<MockPluginInfoResult> listFrom(final List<MockPluginInfo> mockPluginInfoList) {
        if (CollectionUtils.isEmpty(mockPluginInfoList)) {
            return Collections.emptyList();
        }
        return mockPluginInfoList
                .stream()
                .map(e -> {
                    final MockPluginInfoResult res = new MockPluginInfoResult();
                    res.setPluginCode(e.getPluginCode());
                    res.setPluginName(e.getPluginName());
                    res.setDescription(e.getDescription());
                    res.setMetadata(PluginMetaDataResult.from(e.getMetadata()));
                    return res;
                })
                .collect(Collectors.toList());
    }
}