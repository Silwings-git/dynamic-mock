package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;

/**
 * @ClassName UpdateResponseEnableStatusParam
 * @Description
 * @Author Silwings
 * @Date 2023/8/10 12:22
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "修改响应开关参数")
public class UpdateResponseEnableStatusParam {

    @ApiModelProperty(value = "处理器ID", required = true, example = "10")
    private Identity handlerId;

    @ApiModelProperty(value = "响应ID", required = true, example = "10")
    private Identity responseId;

    @ApiModelProperty(value = "启用状态.1-启用,2-禁用", required = true, example = "enable")
    private Integer enableStatus;

    public void validate() {
        CheckUtils.isNotNull(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "handlerId"));
        CheckUtils.isNotNull(this.responseId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "responseId"));
        CheckUtils.isNotNull(EnableStatus.valueOfCode(this.enableStatus), DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "enableStatus"));
    }

}