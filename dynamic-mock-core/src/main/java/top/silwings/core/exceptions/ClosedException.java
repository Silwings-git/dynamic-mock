package top.silwings.core.exceptions;

/**
 * @ClassName ClosedException
 * @Description 已关闭异常
 * @Author Silwings
 * @Date 2023/6/12 13:14
 * @Since
 **/
public class ClosedException extends BaseDynamicMockException {
    public ClosedException(final String message) {
        super(message);
    }
}