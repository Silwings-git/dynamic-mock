package top.silwings.admin.exceptions;

/**
 * @ClassName UserAuthException
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 13:29
 * @Since
 **/
public class UserAuthException extends DynamicMockAdminException {
    public UserAuthException(final String message) {
        super(message);
    }
}