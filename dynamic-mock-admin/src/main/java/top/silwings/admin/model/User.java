package top.silwings.admin.model;

import lombok.Builder;
import lombok.Getter;
import top.silwings.admin.repository.db.mysql.po.UserPo;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.JsonUtils;

import java.util.Map;

/**
 * @ClassName User
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 16:21
 * @Since
 **/
@Getter
@Builder
public class User {

    private static final String DEFAULT_PASSWORD = "root";

    private final Identity userId;

    private final String username;

    private final String userAccount;

    private final String password;

    private final int role;

    public static User from(final UserPo userPo) {
        return User.builder()
                .userId(Identity.from(userPo.getUserId()))
                .username(userPo.getUsername())
                .userAccount(userPo.getUserAccount())
                .password(userPo.getPassword())
                .role(userPo.getRole())
                .build();
    }

    public static User from(final String json) {

        final Map<String, Object> map = JsonUtils.toMap(json, String.class, Object.class);

        return User.builder()
                .userId(Identity.from(String.valueOf(map.get("userId"))))
                .username(String.valueOf(map.get("username")))
                .userAccount(String.valueOf(map.get("userAccount")))
                .password(String.valueOf(map.get("password")))
                .role(Integer.parseInt(String.valueOf(map.get("role"))))
                .build();
    }

    public UserPo toUser() {
        final UserPo userPo = new UserPo();
        if (null != this.userId) {
            userPo.setUserId(this.userId.intValue());
        }
        userPo.setUsername(this.username);
        userPo.setUserAccount(this.userAccount);
        userPo.setPassword(this.password);
        return userPo;
    }

}