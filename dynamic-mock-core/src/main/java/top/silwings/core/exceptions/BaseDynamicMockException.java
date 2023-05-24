package top.silwings.core.exceptions;

/**
 * @ClassName BaseDynamicMockException
 * @Description 基础异常类型
 * @Author Silwings
 * @Date 2023/5/16 23:44
 * @Since
 **/
public abstract class BaseDynamicMockException extends RuntimeException {

    public BaseDynamicMockException() {
        super();
    }

    public BaseDynamicMockException(final String message) {
        super(message);
    }

    public BaseDynamicMockException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BaseDynamicMockException(final Throwable cause) {
        super(cause);
    }

}