package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.silwings.core.common.Identity;

/**
 * @ClassName QueryTaskParam
 * @Description 查询任务列表参数
 * @Author Silwings
 * @Date 2022/11/23 20:46
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "查询任务列表参数")
public class QueryTaskParam {

    @ApiModelProperty(value = "项目id", required = true, example = "1")
    private Identity projectId;

    @ApiModelProperty(value = "Mock 处理器Id", required = true, example = "1")
    private Identity handlerId;

}