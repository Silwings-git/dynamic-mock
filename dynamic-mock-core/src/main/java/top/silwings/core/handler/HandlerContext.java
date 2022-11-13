package top.silwings.core.handler;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName ParameterContext
 * @Description
 * @Author Silwings
 * @Date 2022/10/29 18:44
 * @Since
 **/
@Getter
@Builder
public class HandlerContext {

    /**
     * 自定义空间
     */
    @Setter
    private Map<String, Object> customizeSpace;

    /**
     * HTTP请求信息
     */
    private final RequestInfo requestInfo;

    public static HandlerContext from(final HttpServletRequest request) {
        return HandlerContext.builder()
                .customizeSpace(Collections.emptyMap())
                .requestInfo(RequestInfo.from(request))
                .build();
    }

    public void addCustomizeParam(final String key, final Object value) {
        this.customizeSpace.put(key, value);
    }

    public Object searchParameter(final String parameterName) {
        return this.customizeSpace.get(parameterName);
    }

    @Getter
    @Setter
    @Builder
    public static class RequestInfo {

        /**
         * 授权类型
         */
        private final String authType;

        /**
         * 上下文路径
         */
        private final String contextPath;

        /**
         * cookie
         */
        private final List<Cookie> cookies;

        /**
         * 请求头
         */
        private final List<Map<String, String>> headers;

        /**
         * 请求方法
         */
        private final String method;

        /**
         * pathInfo
         */
        private final String pathInfo;

        /**
         * pathTranslated
         */
        private final String pathTranslated;

        /**
         * queryString
         */
        private final String queryString;

        /**
         * remoteUser
         */
        private final String remoteUser;

        /**
         * 请求URI
         */
        private final String requestURI;

        /**
         * 请求URL
         */
        private final String requestURL;

        /**
         * servletPath
         */
        private final String servletPath;

        /**
         * 请求体
         */
        private final Object body;

        public static RequestInfo from(final HttpServletRequest request) {

            final Cookie[] cookieArray = request.getCookies();

            String bodyStr = null;
            try {
                bodyStr = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException e) {
                bodyStr = "Failed to parse the request body.";
            }

            return RequestInfo.builder()
                    .authType(request.getAuthType())
                    .contextPath(request.getContextPath())
                    .cookies(null == cookieArray ? Collections.emptyList() : Arrays.asList(cookieArray))
                    .headers(getHeaders(request))
                    .method(request.getMethod())
                    .pathInfo(request.getPathInfo())
                    .pathTranslated(request.getPathTranslated())
                    .queryString(request.getQueryString())
                    .remoteUser(request.getRemoteUser())
                    .requestURI(request.getRequestURI())
                    .requestURL(request.getRequestURL().toString())
                    .servletPath(request.getServletPath())
                    .body(JSON.isValidObject(bodyStr) ? JSON.parseObject(bodyStr) : bodyStr)
                    .build();
        }

        private static List<Map<String, String>> getHeaders(final HttpServletRequest request) {
            final List<Map<String, String>> list = new ArrayList<>();
            final Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                final String nextElement = headerNames.nextElement();
                final Enumeration<String> headers = request.getHeaders(nextElement);
                while (headers.hasMoreElements()) {
                    final Map<String, String> map = new HashMap<>(2);
                    map.put(nextElement, headers.nextElement());
                    list.add(map);
                }
            }
            return list;
        }

    }

}