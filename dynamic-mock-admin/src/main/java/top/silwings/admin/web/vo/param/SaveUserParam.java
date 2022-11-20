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
public class SaveUserParam {

    @ApiModelProperty(value = "用户id", required = true, example = "username")
    private String userId;

    @ApiModelProperty(value = "用户昵称", required = true, example = "username")
    private String username;

    @ApiModelProperty(value = "账号.不支持更新", required = true, example = "userAccount")
    private String userAccount;

    @ApiModelProperty(value = "密码.新增必填,修改时不填表示不更新", required = true, example = "password")
    private String password;

    @ApiModelProperty(value = "角色", required = true, example = "user")
    private String role;
}