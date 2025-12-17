package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.core.common.EnableStatus;
import cn.silwings.core.common.Identity;
import cn.silwings.core.utils.CheckUtils;

/**
 * @ClassName UpdateTaskEnableStatusParam
 * @Description
 * @Author Silwings
 * @Date 2023/8/10 12:22
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "修改任务开关参数")
public class UpdateTaskEnableStatusParam {

    @ApiModelProperty(value = "处理器ID", required = true, example = "10")
    private Identity handlerId;

    @ApiModelProperty(value = "响应ID", required = true, example = "10")
    private Identity taskId;

    @ApiModelProperty(value = "启用状态.1-启用,2-禁用", required = true, example = "enable")
    private Integer enableStatus;

    public void validate() {
        CheckUtils.isNotNull(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "handlerId"));
        CheckUtils.isNotNull(this.taskId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "taskId"));
        CheckUtils.isNotNull(EnableStatus.valueOfCode(this.enableStatus), DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "enableStatus"));
    }

}