package top.silwings.core.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import top.silwings.core.config.MockHandlerHolder;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;
import top.silwings.core.utils.PathMatcherUtils;

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
import java.util.concurrent.ConcurrentHashMap;
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
     * HTTP请求信息
     */
    private final RequestInfo requestInfo;
    /**
     * 本地缓存,可用于运行时函数向其中读写数据
     */
    private final Map<Object, Object> localCache = new ConcurrentHashMap<>();
    /**
     * 自定义空间
     */
    @Setter
    private Map<String, Object> customizeSpace;

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
         * cookie key value映射集
         */
        private final Map<String, String> cookieMap;

        /**
         * 请求头
         */
        private final List<Map<String, String>> headers;

        /**
         * 请求头key value映射
         */
        private final Map<String, String> headerMap;

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

        /**
         * 路径参数
         */
        private final Map<String, String> pathParameterMap;

        public static RequestInfo from(final HttpServletRequest request) {

            final RequestInfoBuilder builder = RequestInfo.builder();

            buildBodyAndParameter(request, builder);

            final AntPathMatcher antPathMatcher = PathMatcherUtils.matcher();

            // MockHandler支持的uri
            final String mockHandlerRequestUri = ConvertUtils.getNoNullOrDefault(MockHandlerHolder.get(), "", MockHandler::getRequestUri);
            final String requestURI = request.getRequestURI();

            Map<String, String> pathParameterMap = Collections.emptyMap();
            if (antPathMatcher.match(mockHandlerRequestUri, requestURI)) {
                // 需要使用经过?分割后的url数据,否者可能将?后的参数错误解析到path参数中
                pathParameterMap = antPathMatcher.extractUriTemplateVariables(mockHandlerRequestUri, requestURI);
            }

            final Cookie[] cookieArray = request.getCookies();

            return builder
                    .authType(request.getAuthType())
                    .contextPath(request.getContextPath())
                    .cookies(null == cookieArray ? Collections.emptyList() : Arrays.stream(cookieArray).collect(Collectors.toList()))
                    .cookieMap(getCookieMap(cookieArray))
                    .headers(getHeaders(request))
                    .headerMap(getHeaderMap(request))
                    .method(request.getMethod())
                    .pathInfo(request.getPathInfo())
                    .pathTranslated(request.getPathTranslated())
                    .queryString(request.getQueryString())
                    .remoteUser(request.getRemoteUser())
                    .requestURI(requestURI)
                    .requestURL(request.getRequestURL().toString())
                    .servletPath(request.getServletPath())
                    .pathParameterMap(pathParameterMap)
                    .build();
        }

        private static Map<String, String> getCookieMap(final Cookie[] cookieArray) {
            return null == cookieArray ? Collections.emptyMap() : Arrays.stream(cookieArray).collect(Collectors.toMap(Cookie::getName, Cookie::getValue, (v1, v2) -> v2));
        }

        private static void buildBodyAndParameter(final HttpServletRequest request, final RequestInfoBuilder builder) {
            final Map<String, List<String>> parameters = new HashMap<>();
            builder.parameterMap(parameters)
                    .body(Collections.emptyMap())
                    .jsonBody(Collections.emptyMap())
                    .textBody("")
                    .formBody(Collections.emptyMap());

            final String contentType = request.getContentType();
            if (StringUtils.isBlank(contentType) || !contentType.contains("form-data")) {

                buildBody(request, builder, contentType);

            }

            final Map<String, String[]> map = request.getParameterMap();
            map.forEach((key, value) -> parameters.put(key, Arrays.stream(value).collect(Collectors.toList())));
        }

        private static void buildBody(final HttpServletRequest request, final RequestInfoBuilder builder, final String contentType) {
            String bodyStr = "";
            try {
                bodyStr = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException e) {
                log.error("Request information parsing failed.", e);
            }
            if (StringUtils.isBlank(contentType)) {

                // contentType为空时,默认使用json/text解析
                buildBlankContentTypeBody(builder, bodyStr);

            } else if (contentType.contains("x-www-form-urlencoded")) {

                buildFormUrlencodedBody(builder, bodyStr);

            } else if (contentType.contains("json")) {

                buildJsonBody(builder, bodyStr);

            } else {

                builder.body(bodyStr)
                        .textBody(bodyStr);
            }
        }

        private static void buildBlankContentTypeBody(final RequestInfoBuilder builder, final String bodyStr) {
            if (JsonUtils.isValidJson(bodyStr)) {
                final Map<String, ?> jsonBody = JsonUtils.toMap(bodyStr, String.class, Object.class);
                builder.body(jsonBody)
                        .jsonBody(jsonBody);
            } else {
                builder.body(bodyStr)
                        .textBody(bodyStr);
            }
        }

        private static void buildJsonBody(final RequestInfoBuilder builder, final String bodyStr) {
            if (JsonUtils.isValidJson(bodyStr)) {
                final Map<String, ?> jsonBody = JsonUtils.toMap(bodyStr, String.class, Object.class);
                builder.body(jsonBody)
                        .jsonBody(jsonBody);
            } else {
                log.error("The request body does not have a valid data in json format.");
            }
        }

        private static void buildFormUrlencodedBody(final RequestInfoBuilder builder, final String bodyStr) {
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
        }

        private static List<Map<String, String>> getHeaders(final HttpServletRequest request) {
            final List<Map<String, String>> list = new ArrayList<>();
            final Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                final String nextElement = headerNames.nextElement();
                final Enumeration<String> headers = request.getHeaders(nextElement);
                while (headers.hasMoreElements()) {
                    final Map<String, String> map = new HashMap<>(1);
                    map.put(nextElement, headers.nextElement());
                    list.add(map);
                }
            }
            return list;
        }

        private static Map<String, String> getHeaderMap(final HttpServletRequest request) {
            final Map<String, String> headerMap = new HashMap<>();
            final Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                final String nextElement = headerNames.nextElement();
                final Enumeration<String> headers = request.getHeaders(nextElement);
                while (headers.hasMoreElements()) {
                    headerMap.put(nextElement, headers.nextElement());
                }
            }
            return headerMap;
        }

    }

}