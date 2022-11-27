package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.core.common.Identity;

/**
 * @ClassName QueryOwnMockHandlerParam
 * @Description 查询拥有的handler参数
 * @Author Silwings
 * @Date 2022/11/27 13:59
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "查询拥有的handler参数")
public class QueryOwnMockHandlerParam {

    @ApiModelProperty(value = "项目id", example = "erp")
    private Identity projectId;

}