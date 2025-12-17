package cn.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.silwings.admin.model.UserDto;
import cn.silwings.core.common.Identity;

import java.util.List;

/**
 * @ClassName UserResult
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 19:44
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "用户信息")
public class UserResult {

    @ApiModelProperty(value = "用户id", example = "1")
    private Identity userId;

    @ApiModelProperty(value = "用户名", example = "Misaka Mikoto")
    private String username;

    @ApiModelProperty(value = "账号", example = "123456789")
    private String userAccount;

    @ApiModelProperty(value = "角色code", example = "123456789")
    private int role;

    @ApiModelProperty(value = "用户权限集合")
    private List<Identity> permissionList;

    public static UserResult from(final UserDto user) {

        final UserResult userResult = new UserResult();
        userResult.setUserId(user.getUserId());
        userResult.setUsername(user.getUsername());
        userResult.setUserAccount(user.getUserAccount());
        userResult.setRole(user.getRole());
        userResult.setPermissionList(user.getPermissionList());
        return userResult;
    }
}