package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.utils.CheckUtils;

/**
 * @ClassName EnableStatusVo
 * @Description 启用禁用Mock处理器参数
 * @Author Silwings
 * @Date 2022/11/16 21:45
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "启用/停用模拟处理器参数")
public class EnableStatusParam {

    @ApiModelProperty(value = "处理器ID", required = true, example = "10")
    private String handlerId;

    @ApiModelProperty(value = "启用状态.enable-启用,disable-禁用", required = true, example = "enable")
    private Integer enableStatus;

    public void validate() {
        CheckUtils.isNotBlank(this.handlerId, () -> DynamicMockAdminException.from("HandlerId cannot be empty."));
        CheckUtils.isNotNull(EnableStatus.valueOfCode(this.enableStatus), () -> DynamicMockAdminException.from("EnableStatus cannot be empty."));
    }
}