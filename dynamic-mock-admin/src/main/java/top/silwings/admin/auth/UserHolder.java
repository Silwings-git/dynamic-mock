package top.silwings.admin.auth;

import top.silwings.admin.exceptions.UserNotLoggedOnException;
import top.silwings.core.common.Identity;

/**
 * @ClassName UserHolder
 * @Description 用户支持
 * @Author Silwings
 * @Date 2022/11/19 12:44
 * @Since
 **/
public class UserHolder {

    private static final ThreadLocal<UserAuthInfo> TL = new ThreadLocal<>();

    private UserHolder() {
        throw new AssertionError();
    }

    public static UserAuthInfo getUser() {
        return getUser(true);
    }

    public static void setUser(final UserAuthInfo userAuthInfo) {
        TL.remove();
        TL.set(userAuthInfo);
    }

    public static Identity getUserId() {
        return getUser(true).getUserId();
    }

    public static UserAuthInfo getUser(final boolean required) {

        final UserAuthInfo user = TL.get();

        if (required && null == user) {
            throw new UserNotLoggedOnException();
        }

        return user;
    }

    public static void remove() {
        TL.remove();
    }

    public static boolean isAdminUser() {
        return getUser().isAdminUser();
    }

    public static void validPermission(final Identity projectId) {
        getUser().validPermission(projectId);
    }

    public static void validProjectId(final Identity projectId) {
        getUser().validProjectId(projectId);
    }

    public static void validHandlerId(final Identity handlerId) {
        getUser().validHandlerId(handlerId);
    }

}