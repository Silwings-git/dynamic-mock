package top.silwings.admin.common;

/**
 * @ClassName Role
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 19:46
 * @Since
 **/
public enum Role {

    // 普通用户
    USER,

    // 管理员用户
    ADMIN_USER,
    ;

    public boolean equalsCode(final String role) {
        return this.name().equalsIgnoreCase(role);
    }

}
