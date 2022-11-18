package top.silwings.core.common;

/**
 * @ClassName EnableStatus
 * @Description 启用状态
 * @Author Silwings
 * @Date 2022/11/16 20:55
 * @Since
 **/
public enum EnableStatus {

    ENABLE("ENABLE"),
    DISABLE("DISABLE"),
    ;

    private final String code;

    EnableStatus(final String code) {
        this.code = code;
    }

    public String code() {
        return this.code;
    }

    public static EnableStatus valueOfCode(final String code) {
        for (final EnableStatus status : values()) {
            if (status.code().equalsIgnoreCase(code)) {
                return status;
            }
        }
        return null;
    }

}
