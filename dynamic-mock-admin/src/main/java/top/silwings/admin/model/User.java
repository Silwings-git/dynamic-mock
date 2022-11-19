package top.silwings.admin.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.DigestUtils;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.repository.db.mysql.po.UserPo;
import top.silwings.core.utils.CheckUtils;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName User
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 16:21
 * @Since
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
public class User {

    private static final String DEFAULT_PASSWORD = "root";

    private String username;

    private String userAccount;

    private String password;

    private String role;

    public static User from(final UserPo userPo) {
        return new User()
                .setUsername(userPo.getUsername())
                .setUserAccount(userPo.getUserAccount())
                .setPassword(userPo.getPassword())
                .setRole(userPo.getRole());
    }

    public static User newUser(final String username, final String userAccount, final String role) {
        return new User()
                .setUsername(username)
                .setUserAccount(userAccount)
                .setPassword(encryptPassword(DEFAULT_PASSWORD))
                .setRole(role);
    }

    public UserPo toUser() {
        final UserPo userPo = new UserPo();
        userPo.setUsername(this.getUsername());
        userPo.setUserAccount(this.getUserAccount());
        userPo.setPassword(this.getPassword());
        return userPo;
    }

    public void changePassword(final String oldPassword, final String newPassword) {

        CheckUtils.isEquals(this.getPassword(), oldPassword, () -> DynamicMockAdminException.from("Password error."));

        this.setPassword(encryptPassword(newPassword));
    }

    public static String encryptPassword(final String newPassword) {
        return DigestUtils.md5DigestAsHex(newPassword.getBytes(StandardCharsets.UTF_8));
    }

    public String getDefaultPassword() {
        return DEFAULT_PASSWORD;
    }

}