package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.core.common.Identity;
import top.silwings.core.handler.task.AutoCancelCronTask;
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

    @ApiModelProperty(value = "任务原始信息", example = JsonUtils.EMPTY_OBJECT)
    private Map<String, ?> taskInfo;

    public static RunningTaskResult from(final AutoCancelCronTask autoCancelCronTask) {
        final RunningTaskResult runningTaskResult = new RunningTaskResult();
        runningTaskResult.setTaskCode(autoCancelCronTask.getRegistrationInfo().getTaskCode());
        runningTaskResult.setTaskName(String.valueOf(JsonUtils.jsonPathRead(autoCancelCronTask.getTaskJson(), "$.name")));
        runningTaskResult.setHandlerId(autoCancelCronTask.getHandlerId());
        runningTaskResult.setRemainingTimes(autoCancelCronTask.getNumberOfExecute().get());
        runningTaskResult.setTaskInfo(JsonUtils.toMap(autoCancelCronTask.getTaskJson(), String.class, Object.class));
        runningTaskResult.setRegistrationTime(new Date(autoCancelCronTask.getRegistrationInfo().getRegistrationTime()));
        return runningTaskResult;
    }

}