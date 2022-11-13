package top.silwings.core.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
@ApiModel(description = "Task信息")
public class TaskInfoVo {

    @ApiModelProperty(value = "任务名称", required = true, example = "会员卡支付回调")
    private String name;

    @ApiModelProperty(value = "支持表达式")
    private List<String> support;

    @ApiModelProperty(value = "是否为异步", required = true, example = "true")
    private Boolean async;

    @ApiModelProperty(value = "Cron表达式", required = true, example = "* * * * * ?")
    private String cron;

    @ApiModelProperty(value = "可执行次数", required = true, example = "1")
    private Integer numberOfExecute;

    @ApiModelProperty(value = "请求信息", required = true)
    private TaskRequestInfoVo request;

}