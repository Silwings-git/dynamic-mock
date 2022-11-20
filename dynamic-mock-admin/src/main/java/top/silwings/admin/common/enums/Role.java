package top.silwings.admin.common.enums;

import lombok.Getter;

/**
 * @ClassName Role
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 19:46
 * @Since
 **/
@Getter
public enum Role {
    // 管理员用户
    ADMIN_USER(1),

    // 普通用户
    USER(2),
    ;

    private final int code;

    Role(final int code) {
        this.code = code;
    }

    public boolean equalsCode(final Integer code) {
        return null != code && this.code == code;
    }

}
