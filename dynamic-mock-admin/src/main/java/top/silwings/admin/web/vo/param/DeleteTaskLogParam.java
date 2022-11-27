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

    @ApiModelProperty(value = "删除的类型.1-删除单条日志,2按handler删除日志,3按项目删除日志", example = "1")
    private String deleteType;

    @ApiModelProperty(value = "处理器id", required = true, example = "1")
    private Identity projectId;

    @ApiModelProperty(value = "处理器id", required = true, example = "1")
    private Identity handlerId;

    @ApiModelProperty(value = "日志id.仅传递handlerId时表示删除该id下全部日志", example = "1")
    private Identity logId;

    public void validate() {

        final DeleteTaskLogType type = DeleteTaskLogType.valueOfCode(this.deleteType);

        CheckUtils.isNotNull(type, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "deleteType"));

        if (DeleteTaskLogType.LOG.equals(type)) {

            // 单条删除
            CheckUtils.isNotNull(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "handlerId"));
            CheckUtils.isNotNull(this.logId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "logId"));

        } else if (DeleteTaskLogType.MOCK_HANDLER.equals(type)) {

            // 按handler删除
            CheckUtils.isNotNull(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "handlerId"));

        } else {

            // 按项目删除
            CheckUtils.isNotNull(this.projectId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "projectId"));
        }

    }

}