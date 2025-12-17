package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.core.utils.CheckUtils;

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

    @ApiModelProperty(value = "记住密码", example = "true")
    private Boolean remember;

    public void validate() {
        CheckUtils.isNotBlank(this.userAccount, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "userAccount"));
        CheckUtils.isNotBlank(this.password, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "password"));
    }
}