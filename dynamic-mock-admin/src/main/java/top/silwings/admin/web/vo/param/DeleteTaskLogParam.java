package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.common.DeleteTaskLogType;
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

    @ApiModelProperty(value = "处理器id", example = "1")
    private Identity projectId;

    @ApiModelProperty(value = "处理器id", example = "1")
    private Identity handlerId;

    @ApiModelProperty(value = "日志id", example = "1")
    private Identity logId;

    @ApiModelProperty(value = "删除方式." +
                              "1-清理一个月之前的数据," +
                              "2-清理三个月之前的数据," +
                              "3-清理六个月之前的数据," +
                              "4-清理一年之前的数据," +
                              "5-清理一千条之前的数据," +
                              "6-清理一万条之前的数据," +
                              "7-清理三万条之前的数据," +
                              "8-清理十万条之前的数据," +
                              "100-清理所有日志", required = true, example = "1")
    private String deleteType;

    public void validate() {
        CheckUtils.isNotNull(DeleteTaskLogType.valueOfCode(this.deleteType), DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "deleteType"));
    }

}