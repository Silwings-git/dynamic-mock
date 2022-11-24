package top.silwings.core.exceptions;

/**
 * @ClassName TypeConversionException
 * @Description
 * @Author Silwings
 * @Date 2022/11/20 20:53
 * @Since
 **/
public class TypeCastException extends DynamicMockException {

    public TypeCastException(final String message) {
        super(message);
    }

    public static TypeCastException from(final String message) {
        return new TypeCastException(message);
    }

}