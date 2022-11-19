package top.silwings.admin.auth;

import lombok.Builder;
import lombok.Getter;
import top.silwings.admin.common.Role;
import top.silwings.admin.model.User;

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

    private String userAccount;

    private String password;

    private String role;

    public static UserAuthInfo from(final User user) {
        return UserAuthInfo.builder()
                .userAccount(user.getUserAccount())
                .password(user.getPassword())
                .role(user.getRole()).build();
    }

    public boolean isAdminUser() {
        return Role.ADMIN_USER.equalsCode(this.role);
    }

}