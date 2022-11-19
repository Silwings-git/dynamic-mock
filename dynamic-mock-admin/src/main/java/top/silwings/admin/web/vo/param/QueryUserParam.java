package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.common.PageParam;

/**
 * @ClassName QueryUserParam
 * @Description 分页查询用户列表参数
 * @Author Silwings
 * @Date 2022/11/19 19:53
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "分页查询用户列表参数")
public class QueryUserParam extends PageParam {

    @ApiModelProperty(value = "模糊查询key,支持用户名和用户账号", required = true, example = "Misaka Mikoto")
    private String searchKey;

}