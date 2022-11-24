package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;

/**
 * @ClassName QueryTaskLogParam
 * @Description 分页查询任务日志参数
 * @Author Silwings
 * @Date 2022/11/24 21:50
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "分页查询任务日志参数")
public class QueryTaskLogParam extends PageParam {

    @ApiModelProperty(value = "处理器id", required = true, example = "1")
    private Identity handlerId;

    @ApiModelProperty(value = "任务名称", required = true, example = "1")
    private String name;

    public void validate() {
        CheckUtils.isNotNull(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "handlerId"));
    }

}