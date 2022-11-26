package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.model.UserDto;
import top.silwings.core.common.Identity;

/**
 * @ClassName UserResult
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 19:44
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "登录返回信息")
public class LoginResult {

    @ApiModelProperty(value = "用户id", example = "1")
    private Identity userId;

    @ApiModelProperty(value = "用户名", example = "Misaka Mikoto")
    private String username;

    @ApiModelProperty(value = "账号", example = "123456789")
    private String userAccount;

    @ApiModelProperty(value = "角色code", example = "123456789")
    private int role;

    public static LoginResult from(final UserDto user) {

        final LoginResult userResult = new LoginResult();
        userResult.setUserId(user.getUserId());
        userResult.setUsername(user.getUsername());
        userResult.setUserAccount(user.getUserAccount());
        userResult.setRole(user.getRole());
        return userResult;
    }
}