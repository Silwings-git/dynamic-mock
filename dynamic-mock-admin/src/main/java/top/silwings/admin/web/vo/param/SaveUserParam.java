package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;

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

    @ApiModelProperty(value = "用户id.有值为更新,无值为新增", example = "10")
    private Identity userId;

    @ApiModelProperty(value = "用户昵称", required = true, example = "username")
    private String username;

    @ApiModelProperty(value = "账号.不支持更新", required = true, example = "userAccount")
    private String userAccount;

    @ApiModelProperty(value = "密码.新增必填,修改时不填表示不更新", example = "password")
    private String password;

    @ApiModelProperty(value = "角色", required = true, example = "user")
    private Integer role;

    public void validate() {
        CheckUtils.isNotBlank(this.username, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "username"));
        CheckUtils.maxLength(this.username, 32, DynamicMockAdminException.supplier(ErrorCode.VALID_TOO_LONG, "username"));
        CheckUtils.isNotNull(this.role, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "role"));

        if (StringUtils.isNotBlank(this.userAccount)) {
            CheckUtils.minLength(this.userAccount, 4, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "userAccount"));
            CheckUtils.maxLength(this.userAccount, 32, DynamicMockAdminException.supplier(ErrorCode.VALID_TOO_LONG, "username"));
        }

        // 更新用户时密码允许为空
        if (null != this.userId && StringUtils.isBlank(this.password)) {
            return;
        }

        CheckUtils.minLength(this.password, 4, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "password"));
        CheckUtils.maxLength(this.password, 16, DynamicMockAdminException.supplier(ErrorCode.VALID_TOO_LONG, "password"));
    }
}