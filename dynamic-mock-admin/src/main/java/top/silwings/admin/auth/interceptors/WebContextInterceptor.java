package top.silwings.admin.auth.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import top.silwings.admin.auth.WebContext;
import top.silwings.admin.auth.WebContextHolder;

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
public class WebContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {

        WebContextHolder.remove();

        final WebContext.WebContextBuilder builder = WebContext.builder();

        final Enumeration<String> headers = request.getHeaders("Accept-Language");
        if (headers.hasMoreElements()) {
            final String language = headers.nextElement();
            builder.language(language);
        }

        WebContextHolder.setContext(builder.build());

        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {
        WebContextHolder.remove();
    }
}