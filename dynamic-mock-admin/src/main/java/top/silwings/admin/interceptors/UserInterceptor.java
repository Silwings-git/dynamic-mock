package top.silwings.admin.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import top.silwings.admin.auth.UserAuthInfo;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.common.Payload;
import top.silwings.admin.utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * @ClassName UserInterceptor
 * @Description 用户拦截器
 * @Author Silwings
 * @Date 2022/11/19 12:48
 * @Since
 **/
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {

        UserHolder.removeUser();

        final Enumeration<String> headers = request.getHeaders("user-auth-token");

        if (headers.hasMoreElements()) {
            final String jwtToken = headers.nextElement();
            final Payload<UserAuthInfo> infoFromToken = JwtUtils.getInfoFromToken(jwtToken, UserAuthInfo.class);

            final UserAuthInfo userInfo = infoFromToken.getUserInfo();

            UserHolder.setUser(userInfo);
        }

        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}