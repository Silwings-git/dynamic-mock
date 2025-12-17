package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.silwings.admin.common.PageParam;
import cn.silwings.core.common.Identity;

/**
 * @ClassName QueryTaskLogParam
 * @Description 分页查询任务日志参数
 * @Author Silwings
 * @Date 2022/11/24 21:50
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "分页查询任务日志参数")
public class QueryTaskLogParam extends PageParam {

    @ApiModelProperty(value = "项目id", example = "1")
    private Identity projectId;

    @ApiModelProperty(value = "处理器id", example = "1")
    private Identity handlerId;

    @ApiModelProperty(value = "任务编码", example = "1")
    private String taskCode;

    @ApiModelProperty(value = "任务名称", example = "1")
    private String name;

}