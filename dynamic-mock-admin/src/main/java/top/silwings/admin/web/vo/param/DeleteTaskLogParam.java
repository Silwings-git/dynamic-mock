package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;

/**
 * @ClassName DeleteTaskLogParam
 * @Description 删除任务日志参数
 * @Author Silwings
 * @Date 2022/11/24 22:14
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "删除任务日志参数")
public class DeleteTaskLogParam {

    @ApiModelProperty(value = "处理器id", required = true, example = "1")
    private Identity handlerId;

    @ApiModelProperty(value = "日志id.仅传递handlerId时表示删除该id下全部日志", example = "1")
    private Identity logId;

    public void validate() {
        CheckUtils.isNotNull(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "handlerId"));
    }

}