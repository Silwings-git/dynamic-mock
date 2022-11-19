package top.silwings.admin.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.model.User;
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

        UserHolder.removeUser();

        boolean needLogin = true;

        if (needLogin) {

            final User user = this.loginService.ifLogin(request, response);

            UserHolder.setUser(user);
        }

        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}