package top.silwings.admin.utils;

import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

/**
 * Cookie.Util
 *
 * @author xuxueli 2015-12-12 18:01:06
 */
public class CookieUtils {

    // 默认缓存时间,单位/秒, 2H
    private static final int COOKIE_MAX_AGE = Integer.MAX_VALUE;
    // 保存路径,根路径
    private static final String COOKIE_PATH = "/";

    /**
     * 保存 Cookie
     */
    public static void set(final HttpServletResponse response, final String key, final String value, final boolean ifRemember) {
        final int age = ifRemember ? COOKIE_MAX_AGE : -1;
        set(response, key, value, null, COOKIE_PATH, age, true);
    }

    /**
     * 保存 Cookie
     */
    private static void set(final HttpServletResponse response, final String key, final String value, final String domain, final String path, final int maxAge, final boolean isHttpOnly) {
        final Cookie cookie = new Cookie(key, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }

    /**
     * 查询 Cookie value
     */
    public static String getValue(final HttpServletRequest request, final String key) {
        final Cookie cookie = get(request, key);
        return null == cookie ? null : cookie.getValue();
    }

    /**
     * 查询 Cookie
     */
    private static Cookie get(final HttpServletRequest request, final String key) {
        final Cookie[] cookieArray = request.getCookies();

        if (ArrayUtils.isEmpty(cookieArray)) {
            return null;
        }

        return Stream.of(cookieArray)
                .filter(cookie -> cookie.getName().equals(key))
                .findFirst()
                .orElse(null);
    }

    /**
     * 删除 Cookie
     */
    public static void remove(final HttpServletRequest request, final HttpServletResponse response, final String key) {
        final Cookie cookie = get(request, key);
        if (cookie != null) {
            set(response, key, "", null, COOKIE_PATH, 0, true);
        }
    }

}