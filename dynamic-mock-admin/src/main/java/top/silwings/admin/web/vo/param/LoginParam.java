package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.core.utils.CheckUtils;

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
public class LoginParam {

    @ApiModelProperty(value = "账号", required = true, example = "userAccount")
    private String userAccount;

    @ApiModelProperty(value = "密码", required = true, example = "password")
    private String password;

    @ApiModelProperty(value = "下次自动登录", example = "true")
    private Boolean ifRemember;

    public void validate() {
        CheckUtils.isNotBlank(this.userAccount, () -> DynamicMockAdminException.from("The account cannot be empty."));
        CheckUtils.isNotBlank(this.password, () -> DynamicMockAdminException.from("The password cannot be empty."));
    }
}