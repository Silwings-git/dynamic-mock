package top.silwings.core.common;

import lombok.Getter;

/**
 * @ClassName HttpRes
 * @Description
 * @Author Silwings
 * @Date 2022/11/14 23:58
 * @Since
 **/
@Getter
public abstract class WebResult {
    protected static final String SUCCESS = "200";
    protected static final String FAIL = "500";

    protected final String code;

    protected final String errMsg;

    protected WebResult(final String code, final String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }
}