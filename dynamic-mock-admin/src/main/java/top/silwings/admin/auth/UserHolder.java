package top.silwings.admin.auth;

import top.silwings.admin.exceptions.UserAuthException;
import top.silwings.admin.model.User;

/**
 * @ClassName UserHolder
 * @Description 用户支持
 * @Author Silwings
 * @Date 2022/11/19 12:44
 * @Since
 **/
public class UserHolder {

    private static final ThreadLocal<User> TL = new ThreadLocal<>();

    public static void setUser(final User userAuthInfo) {
        TL.remove();
        TL.set(userAuthInfo);
    }

    public static User getUser() {
        return getUser(true);
    }

    public static User getUser(final boolean required) {

        final User user = TL.get();

        if (required && null == user) {
            throw new UserAuthException("Please log on first!");
        }

        return user;
    }

    public static void removeUser() {
        TL.remove();
    }

}