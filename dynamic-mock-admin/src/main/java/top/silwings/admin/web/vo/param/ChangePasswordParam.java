package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
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

    @ApiModelProperty(value = "旧密码,限制长度最少为4", required = true, example = "password")
    private String oldPassword;

    @ApiModelProperty(value = "新密码,限制长度最少为4", required = true, example = "password")
    private String newPassword;

    public void validate() {
        CheckUtils.minLength(this.oldPassword, 4, () -> DynamicMockAdminException.of(ErrorCode.VALID_ERROR, "oldPassword"));
        CheckUtils.minLength(this.newPassword, 4, () -> DynamicMockAdminException.of(ErrorCode.VALID_ERROR, "newPassword"));
    }
}