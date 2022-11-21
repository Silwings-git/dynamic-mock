package top.silwings.admin.auth;

import top.silwings.admin.exceptions.UserAuthException;
import top.silwings.core.common.Identity;

/**
 * @ClassName UserHolder
 * @Description 用户支持
 * @Author Silwings
 * @Date 2022/11/19 12:44
 * @Since
 **/
public class UserHolder {

    private UserHolder() {
        throw new AssertionError();
    }

    private static final ThreadLocal<UserAuthInfo> TL = new ThreadLocal<>();

    public static void setUser(final UserAuthInfo userAuthInfo) {
        TL.remove();
        TL.set(userAuthInfo);
    }

    public static UserAuthInfo getUser() {
        return getUser(true);
    }

    public static Identity getUserId() {
        return getUser(true).getUserId();
    }

    public static UserAuthInfo getUser(final boolean required) {

        final UserAuthInfo user = TL.get();

        if (required && null == user) {
            throw new UserAuthException("Please log on first!");
        }

        return user;
    }

    public static void removeUser() {
        TL.remove();
    }

    public static boolean isAdminUser() {
        return getUser().isAdminUser();
    }

    public static void validPermission(final Identity projectId) {
        getUser().validPermission(projectId);
    }

}