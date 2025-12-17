package cn.silwings.admin.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "PageResult")
public class PageResult<T> extends WebResult {

    @ApiModelProperty(value = "分页数据")
    private final List<T> pageData;

    @ApiModelProperty(value = "总条数")
    private final long total;

    public PageResult(final String code, final String errMsg, final List<T> pageData, final long total) {
        super(code, errMsg);
        this.pageData = pageData;
        this.total = total;
    }

    public static <T> PageResult<T> ok(final List<T> pageData, final long total) {
        return new PageResult<>(SUCCESS, null, pageData, total);
    }

    public static <T> PageResult<T> fail(final String errMsg) {
        return new PageResult<>(FAIL, errMsg, Collections.emptyList(), 0L);
    }

}