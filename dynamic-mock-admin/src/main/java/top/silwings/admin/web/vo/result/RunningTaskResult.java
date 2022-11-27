package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.core.common.Identity;
import top.silwings.core.handler.task.AutoCancelTask;
import top.silwings.core.utils.JsonUtils;

import java.util.Date;
import java.util.Map;

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
public class RunningTaskResult {

    @ApiModelProperty(value = "任务编码", example = "1")
    private String taskCode;

    @ApiModelProperty(value = "任务名称", example = "1")
    private String taskName;

    @ApiModelProperty(value = "处理器id", example = "1")
    private Identity handlerId;

    @ApiModelProperty(value = "剩余执行次数", example = "1")
    private Integer remainingTimes;

    @ApiModelProperty(value = "注册时间", example = "1")
    private Date registrationTime;

    @ApiModelProperty(value = "任务原始信息", example = "{}")
    private Map<String, ?> taskInfo;

    public static RunningTaskResult from(final AutoCancelTask autoCancelTask) {
        final RunningTaskResult runningTaskResult = new RunningTaskResult();
        runningTaskResult.setTaskCode(autoCancelTask.getTaskCode());
        runningTaskResult.setTaskName(String.valueOf(JsonUtils.jsonPathRead(autoCancelTask.getTaskJson(), "$.name")));
        runningTaskResult.setHandlerId(autoCancelTask.getHandlerId());
        runningTaskResult.setRemainingTimes(autoCancelTask.getNumberOfExecute().get());
        runningTaskResult.setTaskInfo(JsonUtils.toMap(autoCancelTask.getTaskJson(), String.class, Object.class));
        runningTaskResult.setRegistrationTime(new Date(autoCancelTask.getRegistrationTime()));
        return runningTaskResult;
    }

}