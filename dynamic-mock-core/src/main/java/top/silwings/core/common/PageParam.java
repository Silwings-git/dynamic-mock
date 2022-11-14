package top.silwings.core.common;

import lombok.Getter;

/**
 * @ClassName PageParam
 * @Description 分页参数
 * @Author Silwings
 * @Date 2022/11/15 0:04
 * @Since
 **/
@Getter
public class PageParam {

    private final int pageNum;

    private final int pageSize;

    public PageParam(final int pageNum, final int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static PageParam of(final int pageNum, final int pageSize) {
        return new PageParam(pageNum, pageSize);
    }
}