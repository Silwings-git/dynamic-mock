package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.core.common.Identity;

/**
 * @ClassName TaskResult
 * @Description 任务信息
 * @Author Silwings
 * @Date 2022/11/23 20:36
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "任务响应信息")
public class TaskResult {

    @ApiModelProperty(value = "任务编码", example = "1")
    private String taskCode;

    @ApiModelProperty(value = "处理器id", example = "1")
    private String handlerId;

    @ApiModelProperty(value = "剩余执行次数", example = "1")
    private Integer remainingTimes;

    @ApiModelProperty(value = "任务原始信息", example = "{}")
    private String taskJson;

    public static TaskResult of(final String taskCode, final Identity handlerId, final Integer remainingTimes, final String taskJson) {
        final TaskResult taskResult = new TaskResult();
        taskResult.setTaskCode(taskCode);
        taskResult.setHandlerId(handlerId.stringValue());
        taskResult.setRemainingTimes(remainingTimes);
        taskResult.setTaskJson(taskJson);
        return taskResult;
    }

}