package top.silwings.admin.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.repository.db.mysql.po.UserPo;
import top.silwings.core.common.Identity;

/**
 * @ClassName User
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 16:21
 * @Since
 **/
@Getter
@Setter(AccessLevel.PRIVATE)
@Builder
public class User {

    private static final String DEFAULT_PASSWORD = "root";

    private Identity userId;

    private String username;

    private String userAccount;

    private String password;

    private String role;

    public static User from(final UserPo userPo) {
        return User.builder()
                .userId(Identity.from(userPo.getId()))
                .username(userPo.getUsername())
                .userAccount(userPo.getUserAccount())
                .password(userPo.getPassword())
                .role(userPo.getRole())
                .build();
    }

    public UserPo toUser() {
        final UserPo userPo = new UserPo();
        if (null != this.getUserId()) {
            userPo.setId(this.getUserId().longValue());
        }
        userPo.setUsername(this.getUsername());
        userPo.setUserAccount(this.getUserAccount());
        userPo.setPassword(this.getPassword());
        return userPo;
    }

}