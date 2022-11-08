package top.silwings.core;

/**
 * @ClassName Parser
 * @Description 解析器接口
 * @Author Silwings
 * @Date 2022/11/8 9:24
 * @Since
 **/
public interface Parser<T, R> {

    boolean support(T t);

    R parse(T t);

}