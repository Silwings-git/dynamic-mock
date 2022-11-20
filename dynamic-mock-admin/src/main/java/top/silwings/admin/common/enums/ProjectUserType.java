package top.silwings.admin.common.enums;

/**
 * @ClassName ProjectUserType
 * @Description
 * @Author Silwings
 * @Date 2022/11/20 14:43
 * @Since
 **/
public enum ProjectUserType {

    MANAGER(1),
    DEVELOPER(2),
    ;

    private final int code;

    ProjectUserType(final int code) {
        this.code = code;
    }

    public static ProjectUserType valueOfCode(final Integer type) {

        for (final ProjectUserType value : values()) {
            if (value.equalsCode(type)) {
                return value;
            }
        }

        return null;
    }

    public boolean equalsCode(final Integer code) {
        return null != code && this.code == code;
    }

    public int code() {
        return this.code;
    }
}