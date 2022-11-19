package top.silwings.admin.model;

import lombok.Builder;
import lombok.Getter;
import top.silwings.admin.repository.db.mysql.po.UserPo;

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

    private String username;

    private String userAccount;

    private String password;

    public static User from(final UserPo userPo) {
        return User.builder()
                .username(userPo.getUsername())
                .userAccount(userPo.getUserAccount())
                .password(userPo.getPassword())
                .build();
    }
}