package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.silwings.admin.common.PageParam;

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

    @ApiModelProperty(value = "用户名", example = "Misaka Mikoto")
    private String username;

    @ApiModelProperty(value = "账号", example = "Misaka Mikoto")
    private String userAccount;

    @ApiModelProperty(value = "角色", example = "1")
    private Integer role;

}