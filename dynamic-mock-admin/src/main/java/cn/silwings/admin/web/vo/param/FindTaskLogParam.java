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
 * @ClassName FindTaskLogParam
 * @Description 查询任务日志参数
 * @Author Silwings
 * @Date 2022/11/27 21:05
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "查询任务日志参数")
public class FindTaskLogParam {

    @ApiModelProperty(value = "处理器id", required = true, example = "1")
    private Identity handlerId;

    @ApiModelProperty(value = "任务编码", required = true, example = "1")
    private Identity logId;

    public void validate() {
        CheckUtils.isNotNull(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "handlerId"));
        CheckUtils.isNotNull(this.logId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "logId"));
    }
}