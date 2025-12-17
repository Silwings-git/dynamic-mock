package cn.silwings.core.exceptions;

/**
 * @ClassName TypeConversionException
 * @Description
 * @Author Silwings
 * @Date 2022/11/20 20:53
 * @Since
 **/
public class TypeCastException extends BaseDynamicMockException {

    public TypeCastException(final String message) {
        super(message);
    }

    public TypeCastException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static TypeCastException from(final String message) {
        return new TypeCastException(message);
    }

    public static TypeCastException of(final String message, final Throwable cause) {
        return new TypeCastException(message, cause);
    }

}