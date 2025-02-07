package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.silwings.core.common.Identity;

import java.util.List;

/**
 * @ClassName TaskInfoVo
 * @Description 任务信息
 * @Author Silwings
 * @Date 2022/11/13 22:52
 * @Since
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Task信息")
public class SaveTaskInfoParam {

    @ApiModelProperty(value = "任务id", example = "1")
    private Identity taskId;

    @ApiModelProperty(value = "任务名称", required = true, example = "会员卡支付回调")
    private String name;

    @ApiModelProperty(value = "启用状态", example = "1")
    private Integer enableStatus;

    @ApiModelProperty(value = "支持表达式")
    private List<String> support;

    @ApiModelProperty(value = "是否为异步", required = true, example = "true")
    private Boolean async;

    @ApiModelProperty(value = "Cron表达式", required = true, example = "* * * * * ?")
    private String cron;

    @ApiModelProperty(value = "可执行次数", required = true, example = "1")
    private Integer numberOfExecute;

    @ApiModelProperty(value = "请求信息", required = true)
    private SaveTaskRequestInfoParam request;

}