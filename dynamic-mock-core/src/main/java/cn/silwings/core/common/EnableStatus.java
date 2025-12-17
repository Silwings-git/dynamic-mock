package cn.silwings.core.common;

/**
 * @ClassName EnableStatus
 * @Description 启用状态
 * @Author Silwings
 * @Date 2022/11/16 20:55
 * @Since
 **/
public enum EnableStatus {

    ENABLE(1),
    DISABLE(2),
    ;

    private final int code;

    EnableStatus(final int code) {
        this.code = code;
    }

    public static EnableStatus valueOfCode(final Integer code) {

        if (null == code) {
            return null;
        }

        for (final EnableStatus status : values()) {
            if (status.code() == code) {
                return status;
            }
        }

        return null;
    }

    public int code() {
        return this.code;
    }

}
