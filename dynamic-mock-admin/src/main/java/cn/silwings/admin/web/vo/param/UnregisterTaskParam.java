package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.core.common.Identity;
import cn.silwings.core.utils.CheckUtils;

/**
 * @ClassName UnregisterTaskParam
 * @Description 取消任务参数
 * @Author Silwings
 * @Date 2022/11/23 21:32
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "取消任务参数")
public class UnregisterTaskParam {

    @ApiModelProperty(value = "处理器id", example = "10")
    private Identity handlerId;

    @ApiModelProperty(value = "任务编码", example = "10")
    private String taskCode;

    @ApiModelProperty(value = "是否强制中断", example = "true")
    private Boolean interrupt;

    public void validate() {
        CheckUtils.isNotBlank(this.taskCode, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "taskCode"));
        CheckUtils.isNotNull(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "handlerId"));
    }

}