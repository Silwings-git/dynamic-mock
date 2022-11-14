package top.silwings.core.common;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName PageResult
 * @Description Web Page Result
 * @Author Silwings
 * @Date 2022/11/14 23:55
 * @Since
 **/
@Getter
public class PageResult<T> extends WebResult {

    private final List<T> pageData;

    private final long total;

    public PageResult(final String code, final String errMsg, final List<T> pageData, final long total) {
        super(code, errMsg);
        this.pageData = pageData;
        this.total = total;
    }

    public static <T> PageResult<T> ok() {
        return new PageResult<>(SUCCESS, null, Collections.emptyList(), 0L);
    }

    public static <T> PageResult<T> ok(final List<T> pageData, final long total) {
        return new PageResult<>(SUCCESS, null, pageData, total);
    }

    public static <T> PageResult<T> fail(final String errMsg) {
        return new PageResult<>(FAIL, errMsg, Collections.emptyList(), 0L);
    }

}