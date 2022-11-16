package top.silwings.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
public class RequestContext {

    /**
     * 自定义空间
     */
    @Setter
    private Map<String, Object> customizeSpace;

    /**
     * HTTP请求信息
     */
    private final RequestInfo requestInfo;

    public static RequestContext from(final HttpServletRequest request) {
        return RequestContext.builder()
                .customizeSpace(Collections.emptyMap())
                .requestInfo(RequestInfo.from(request))
                .build();
    }

    public void addCustomizeParam(final String key, final Object value) {
        this.customizeSpace.put(key, value);
    }

    @Slf4j
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

        /**
         * application/json
         */
        private final Map<String, ?> jsonBody;

        /**
         * text/plain
         */
        private final String textBody;

        /**
         * x-www-form-urlencoded
         */
        private final Map<String, List<String>> formBody;

        /**
         * form-data
         */
        private final Map<String, List<String>> parameterMap;

        public static RequestInfo from(final HttpServletRequest request) {

            final RequestInfoBuilder builder = RequestInfo.builder();

            buildBodyAndParameter(request, builder);

            final Cookie[] cookieArray = request.getCookies();

            return builder
                    .authType(request.getAuthType())
                    .contextPath(request.getContextPath())
                    .cookies(null == cookieArray ? Collections.emptyList() : Arrays.stream(cookieArray).collect(Collectors.toList()))
                    .headers(getHeaders(request))
                    .method(request.getMethod())
                    .pathInfo(request.getPathInfo())
                    .pathTranslated(request.getPathTranslated())
                    .queryString(request.getQueryString())
                    .remoteUser(request.getRemoteUser())
                    .requestURI(request.getRequestURI())
                    .requestURL(request.getRequestURL().toString())
                    .servletPath(request.getServletPath())
                    .build();
        }

        private static void buildBodyAndParameter(final HttpServletRequest request, final RequestInfoBuilder builder) {
            final Map<String, List<String>> parameters = new HashMap<>();
            builder.parameterMap(parameters)
                    .body(Collections.emptyMap())
                    .jsonBody(Collections.emptyMap())
                    .textBody("")
                    .formBody(Collections.emptyMap());

            final String contentType = request.getContentType();
            if (StringUtils.isNotBlank(contentType)) {
                if (contentType.contains("form-data")) {
                    final Map<String, String[]> map = request.getParameterMap();
                    map.forEach((key, value) -> parameters.put(key, Arrays.stream(value).collect(Collectors.toList())));
                } else {
                    buildBody(request, builder, contentType);
                }
            }
        }

        private static void buildBody(final HttpServletRequest request, final RequestInfoBuilder builder, final String contentType) {
            String bodyStr = "";
            try {
                bodyStr = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException e) {
                log.error("解析请求失败.", e);
            }
            if (contentType.contains("x-www-form-urlencoded")) {
                final Map<String, List<String>> formMap = new HashMap<>();
                builder.body(formMap)
                        .formBody(formMap);
                if (StringUtils.isNotBlank(bodyStr)) {
                    final String[] split = bodyStr.split("&");
                    for (final String kv : split) {
                        final String[] kvArray = kv.split("=");
                        final List<String> valueList = formMap.computeIfAbsent(kvArray[0], key -> new ArrayList<>());
                        valueList.add(kvArray.length == 2 ? kvArray[1] : "");
                    }
                }
            } else if (contentType.contains("json")) {
                if (JSON.isValidObject(bodyStr)) {
                    final JSONObject jsonBody = JSON.parseObject(bodyStr);
                    builder.body(jsonBody)
                            .jsonBody(jsonBody);
                }
            } else {
                builder.body(bodyStr)
                        .textBody(bodyStr);
            }
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