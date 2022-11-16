package top.silwings.core.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName EnableStatusVo
 * @Description 启用禁用Mock处理器参数
 * @Author Silwings
 * @Date 2022/11/16 21:45
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "启用/停用模拟处理器参数")
public class EnableStatusVo {

    @ApiModelProperty(value = "处理器ID", required = true, example = "10")
    private String handlerId;

    @ApiModelProperty(value = "启用状态.enable-启用,disable-禁用", required = true, example = "enable")
    private String enableStatus;

}