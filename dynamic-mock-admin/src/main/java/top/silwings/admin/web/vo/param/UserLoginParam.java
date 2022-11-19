package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName UserLoginParamVo
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:29
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "登录参数")
public class UserLoginParam {

    @ApiModelProperty(value = "账号", required = true, example = "userAccount")
    private String userAccount;

    @ApiModelProperty(value = "密码", required = true, example = "password")
    private String password;

    @ApiModelProperty(value = "下次自动登录", example = "true")
    private Boolean ifRemember;

}