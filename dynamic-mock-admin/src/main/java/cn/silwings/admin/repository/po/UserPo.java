package cn.silwings.admin.repository.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName UserInfo
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:59
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "dm_user")
public class UserPo {

    public static final String C_USER_ID = "userId";
    public static final String C_USER_ACCOUNT = "userAccount";
    public static final String C_USERNAME = "username";
    public static final String C_ROLE = "role";

    /**
     * 用户id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer userId;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 用户账户
     */
    @Column(name = "user_account")
    private String userAccount;

    /**
     * 用户密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 角色
     */
    @Column(name = "role")
    private Integer role;

    /**
     * 权限.项目id,多个逗号拼接
     */
    @Column(name = "permission")
    private String permission;

}