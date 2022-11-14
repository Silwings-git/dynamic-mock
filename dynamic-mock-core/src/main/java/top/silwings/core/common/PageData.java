package top.silwings.core.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName PageData
 * @Description
 * @Author Silwings
 * @Date 2022/11/15 0:05
 * @Since
 **/
@Getter
@Setter
public class PageData<T> {

    private List<T> list;

    private long total;

}