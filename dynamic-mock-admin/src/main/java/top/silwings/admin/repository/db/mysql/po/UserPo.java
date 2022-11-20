package top.silwings.admin.repository.db.mysql.po;

import lombok.Getter;
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
@Table(name = "dm_user")
public class UserPo {

    public static final String C_USER_ID = "userId";
    public static final String C_USER_ACCOUNT = "userAccount";
    public static final String C_USERNAME = "username";

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "user_account")
    private String userAccount;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private Integer role;

}