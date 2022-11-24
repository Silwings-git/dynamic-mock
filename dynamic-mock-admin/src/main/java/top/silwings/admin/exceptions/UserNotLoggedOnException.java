package top.silwings.admin.exceptions;

/**
 * @ClassName UserNotLoggedOnException
 * @Description 未登录异常
 * @Author Silwings
 * @Date 2022/11/23 19:37
 * @Since
 **/
public class UserNotLoggedOnException extends DynamicMockAdminException {
    public UserNotLoggedOnException() {
        super(ErrorCode.AUTH_NOT_LOGGED_ON, new String[0]);
    }
}