package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.core.utils.CheckUtils;

import java.util.Map;

/**
 * @ClassName MockHandlerPluginInfoParam
 * @Description
 * @Author Silwings
 * @Date 2023/8/13 12:22
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "插件参数信息")
public class MockHandlerPluginInfoParam {

    @ApiModelProperty(value = "插件编码", required = true, example = "EncryptionPlugin")
    private String pluginCode;

    @ApiModelProperty(value = "启用状态,1-启用,2-禁用.默认启用", example = "1")
    private Integer enableStatus;

    @ApiModelProperty(value = "插件参数")
    private Map<String, ?> pluginParam;

    public void validate() {
        CheckUtils.isNotBlank(this.pluginCode, DynamicMockAdminException.supplier(ErrorCode.UNKNOWN_ERROR, "pluginCode"));
    }
}