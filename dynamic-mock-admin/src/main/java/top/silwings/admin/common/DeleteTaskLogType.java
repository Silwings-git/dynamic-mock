package top.silwings.admin.common;

import lombok.Getter;

/**
 * @ClassName UnregisterType
 * @Description 取消注册类型
 * @Author Silwings
 * @Date 2022/11/23 21:50
 * @Since
 **/
@Getter
public enum DeleteTaskLogType {

    LOG("1"),
    MOCK_HANDLER("2"),
    PROJECT("3"),
    ;

    private final String code;

    DeleteTaskLogType(final String code) {
        this.code = code;
    }

    public static DeleteTaskLogType valueOfCode(final String code) {
        for (final DeleteTaskLogType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

}