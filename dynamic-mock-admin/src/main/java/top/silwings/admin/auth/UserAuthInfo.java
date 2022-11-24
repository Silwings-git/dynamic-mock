package top.silwings.admin.auth;

import lombok.Builder;
import lombok.Getter;
import top.silwings.admin.common.Role;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.UserDto;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName UserAuthInfo
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 18:11
 * @Since
 **/
@Getter
@Builder
public class UserAuthInfo {

    /**
     * 用户id
     */
    private Identity userId;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户角色
     * {@link Role}
     */
    private int role;

    /**
     * 用户权限
     */
    private List<Identity> permissionList;

    public static UserAuthInfo from(final UserDto user) {
        return UserAuthInfo.builder()
                .userId(user.getUserId())
                .userAccount(user.getUserAccount())
                .role(user.getRole())
                .permissionList(ConvertUtils.getNoNullOrDefault(user.getPermissionList(), Collections.emptyList()))
                .build();
    }

    public boolean isAdminUser() {
        return Role.ADMIN_USER.equalsCode(this.role);
    }

    public void validPermission(final Identity projectId) {
        if (!isAdminUser()) {
            CheckUtils.isIn(projectId, this.permissionList, () -> DynamicMockAdminException.from(ErrorCode.AUTH_INSUFFICIENT_PERMISSIONS));
        }
    }

}