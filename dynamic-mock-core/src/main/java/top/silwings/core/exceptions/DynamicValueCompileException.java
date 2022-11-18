package top.silwings.core.exceptions;

/**
 * @ClassName DynamicValueCompileException
 * @Description
 * @Author Silwings
 * @Date 2022/11/18 22:38
 * @Since
 **/
public class DynamicValueCompileException extends DynamicMockException {

    public DynamicValueCompileException(final String message) {
        super(message);
    }

    public static RuntimeException from(final String message) {
        return new DynamicValueCompileException(message);
    }

}