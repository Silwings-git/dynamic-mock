package top.silwings.admin.auth;

import lombok.Builder;
import lombok.Getter;
import top.silwings.admin.common.enums.Role;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.model.User;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;

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

    private Identity userId;

    private String userAccount;

    private String password;

    private int role;

    private List<Identity> permission;

    public static UserAuthInfo from(final User user) {
        return UserAuthInfo.builder()
                .userId(user.getUserId())
                .userAccount(user.getUserAccount())
                .password(user.getPassword())
                .role(user.getRole()).build();
    }

    public boolean isAdminUser() {
        return Role.ADMIN_USER.equalsCode(this.role);
    }

    public void validPermission(final Identity projectId) {
        CheckUtils.isIn(projectId, this.permission, () -> DynamicMockAdminException.from("Insufficient permissions."));
    }

}