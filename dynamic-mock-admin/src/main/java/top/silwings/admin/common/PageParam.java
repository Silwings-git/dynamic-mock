package top.silwings.admin.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.RowBounds;

/**
 * @ClassName PageParam
 * @Description 分页参数
 * @Author Silwings
 * @Date 2022/11/15 0:04
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "分页参数")
public class PageParam {

    private static final int MIN_PAGE_NUM = 1;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 5;

    @ApiModelProperty(value = "页数")
    private int pageNum;

    @ApiModelProperty(value = "每页数量")
    private int pageSize;

    public PageParam() {
        this(MIN_PAGE_NUM, DEFAULT_PAGE_SIZE);
    }

    public PageParam(final int pageNum, final int pageSize) {
        this.pageNum = Math.min(pageNum, MIN_PAGE_NUM);
        this.pageSize = Math.max(pageSize, MAX_PAGE_SIZE);
    }

    public static PageParam of(final int pageNum, final int pageSize) {
        return new PageParam(pageNum, pageSize);
    }

    public RowBounds toRowBounds() {
        return new RowBounds((this.pageNum - 1) * this.pageSize, this.pageSize);
    }
}