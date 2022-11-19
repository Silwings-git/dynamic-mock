package top.silwings.admin.auth;

import top.silwings.admin.exceptions.UserAuthException;

/**
 * @ClassName UserHolder
 * @Description 用户支持
 * @Author Silwings
 * @Date 2022/11/19 12:44
 * @Since
 **/
public class UserHolder {

    private static final ThreadLocal<UserAuthInfo> TL = new ThreadLocal<>();

    public static void setUser(final UserAuthInfo userAuthInfo) {
        TL.remove();
        TL.set(userAuthInfo);
    }

    public static UserAuthInfo getUser() {
        return getUser(true);
    }

    public static UserAuthInfo getUser(final boolean required) {

        final UserAuthInfo userAuthInfo = TL.get();

        if (required && null == userAuthInfo) {
            throw new UserAuthException("Please log on first!");
        }

        return userAuthInfo;
    }

    public static void removeUser() {
        TL.remove();
    }

}