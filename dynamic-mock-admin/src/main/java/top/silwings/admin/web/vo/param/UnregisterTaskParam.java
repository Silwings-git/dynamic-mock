package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.common.UnregisterType;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;

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

    @ApiModelProperty(value = "取消注册的类型.1-取消注册单个任务,2按handler取消注册,3按项目取消注册", example = "1")
    private String unregisterType;

    @ApiModelProperty(value = "项目id", example = "10")
    private Identity projectId;

    @ApiModelProperty(value = "处理器id", example = "10")
    private Identity handlerId;

    @ApiModelProperty(value = "任务编码", example = "10")
    private String taskCode;

    @ApiModelProperty(value = "是否强制中断", example = "10")
    private Boolean interrupt;

    public void validate() {
        final UnregisterType type = UnregisterType.valueOfCode(this.unregisterType);
        CheckUtils.isNotNull(type, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "unregisterType"));

        if (UnregisterType.TASK.equals(type)) {
            CheckUtils.isNotNull(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "handlerId"));
            CheckUtils.isNotBlank(this.taskCode, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "taskCode"));
        } else if (UnregisterType.MOCK_HANDLER.equals(type)) {
            CheckUtils.isNotNull(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "handlerId"));
        } else {
            CheckUtils.isNotNull(this.projectId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "projectId"));
        }

        CheckUtils.isNotNull(this.interrupt, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "interrupt"));
    }

}