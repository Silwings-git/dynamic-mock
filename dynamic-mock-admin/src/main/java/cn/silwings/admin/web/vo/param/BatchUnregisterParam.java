package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.silwings.core.common.Identity;

/**
 * @ClassName BatchUnregisterParam
 * @Description 批量取消注册任务参数
 * @Author Silwings
 * @Date 2022/11/28 1:09
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "批量取消注册任务参数")
public class BatchUnregisterParam {

    @ApiModelProperty(value = "项目id", example = "1")
    private Identity projectId;

    @ApiModelProperty(value = "处理器id", example = "1")
    private Identity handlerId;

    @ApiModelProperty(value = "是否强制中断", example = "true")
    private Boolean interrupt;

}