package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName UserParam
 * @Description 用户信息
 * @Author Silwings
 * @Date 2022/11/19 17:47
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "保存用户参数")
public class UserParam {

    @ApiModelProperty(value = "用户昵称", required = true, example = "username")
    private String username;

    @ApiModelProperty(value = "账号", required = true, example = "userAccount")
    private String userAccount;

    @ApiModelProperty(value = "角色", required = true, example = "user")
    private String role;
}