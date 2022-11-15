package top.silwings.core.common;

import org.apache.ibatis.session.RowBounds;

/**
 * @ClassName PageRowBounds
 * @Description
 * @Author Silwings
 * @Date 2022/11/16 0:06
 * @Since
 **/
public class PageRowBounds extends RowBounds {

    private static final int MIN_OFFSET = 0;
    private static final int MAX_LIMIT = 1000;

    public PageRowBounds(final int offset, final int limit) {
        super(Math.max(offset, MIN_OFFSET), Math.min(limit, MAX_LIMIT));
    }

}