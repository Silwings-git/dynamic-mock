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

    @ApiModelProperty(value = "任务名称", required = true)
    private String name;

    @ApiModelProperty(value = "支持表达式")
    private List<String> support;


}