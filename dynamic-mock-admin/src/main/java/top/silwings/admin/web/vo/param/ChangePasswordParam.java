package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.core.utils.CheckUtils;

/**
 * @ClassName ChangePasswordParam
 * @Description 修改密码参数
 * @Author Silwings
 * @Date 2022/11/19 17:47
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "修改密码参数")
public class ChangePasswordParam {

    @ApiModelProperty(value = "旧密码", required = true, example = "password")
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true, example = "password")
    private String newPassword;

    public void validate() {
        CheckUtils.isNotBlank(this.oldPassword, () -> DynamicMockAdminException.from("The old password cannot be empty."));
        CheckUtils.isNotBlank(this.newPassword, () -> DynamicMockAdminException.from("The new password cannot be empty."));
        CheckUtils.isNotEquals(this.oldPassword, this.newPassword, () -> DynamicMockAdminException.from("New password and old password cannot be the same."));
    }
}