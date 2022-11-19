package top.silwings.admin.common;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName PageData
 * @Description
 * @Author Silwings
 * @Date 2022/11/15 0:05
 * @Since
 **/
@Getter
public class PageData<T> {

    @SuppressWarnings("unchecked")
    private static final PageData EMPTY = new PageData<>(Collections.emptyList(), 0L);

    private final List<T> list;

    private final long total;

    @SuppressWarnings("unchecked")
    public static <T> PageData<T> empty() {
        return (PageData<T>) EMPTY;
    }

    public PageData(final List<T> list, final long total) {
        this.list = list;
        this.total = total;
    }

    public static <T> PageData<T> of(final List<T> list, final long total) {
        return new PageData<>(list, total);
    }

}