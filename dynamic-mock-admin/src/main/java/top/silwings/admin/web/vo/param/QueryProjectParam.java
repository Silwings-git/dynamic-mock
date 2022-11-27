package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.common.PageParam;

/**
 * @ClassName QueryProjectParam
 * @Description 分页查询项目参数
 * @Author Silwings
 * @Date 2022/11/21 21:39
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "查询项目参数")
public class QueryProjectParam extends PageParam {

    @ApiModelProperty(value = "项目名称", required = true, example = "erp")
    private String projectName;

}