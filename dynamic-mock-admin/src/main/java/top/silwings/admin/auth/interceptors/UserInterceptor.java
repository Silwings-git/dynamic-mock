package top.silwings.admin.auth.interceptors;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.silwings.admin.auth.UserAuthInfo;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.exceptions.UserNotLoggedOnException;
import top.silwings.admin.model.UserDto;
import top.silwings.admin.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName UserInterceptor
 * @Description 用户拦截器
 * @Author Silwings
 * @Date 2022/11/19 12:48
 * @Since
 **/
public class UserInterceptor implements HandlerInterceptor {

    private final LoginService loginService;

    public UserInterceptor(final LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {

        UserHolder.remove();

        if (!(handler instanceof HandlerMethod)) {
            return true;    // proceed with the next interceptor
        }

        final String requestURI = request.getRequestURI();

        // 默认拦截所有以/dynamic-mock起始的请求地址
        boolean needLogin = requestURI.startsWith("/dynamic-mock");
        boolean needAdminUser = false;

        final PermissionLimit permission = ((HandlerMethod) handler).getMethodAnnotation(PermissionLimit.class);
        if (permission != null) {
            needLogin = permission.limit();
            needAdminUser = permission.adminUser();
        }

        if (needLogin) {

            final UserDto user = this.loginService.ifLogin(request, response);

            if (null == user) {
                throw new UserNotLoggedOnException();
            }

            final UserAuthInfo userAuthInfo = UserAuthInfo.from(user);

            if (needAdminUser && !userAuthInfo.isAdminUser()) {
                throw DynamicMockAdminException.from(ErrorCode.AUTH_INSUFFICIENT_PERMISSIONS);
            }

            UserHolder.setUser(UserAuthInfo.from(user));
        }

        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {
        UserHolder.remove();
    }
}