package top.silwings.core.web;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.ResponseEntity;
import org.springframework.util.IdGenerator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.handler.task.MockTaskManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
 * @ClassName MockHandlerController
 * @Description MockHandler处理点
 * @Author Silwings
 * @Date 2022/11/10 21:27
 * @Since
 **/
@RestControllerAdvice
public class MockHandlerPoint {

    private final MockHandlerManager mockHandlerManager;

    private final MockTaskManager mockTaskManager;

    private final IdGenerator idGenerator;

    public MockHandlerPoint(final MockHandlerManager mockHandlerManager, final MockTaskManager mockTaskManager, final IdGenerator idGenerator) {
        this.mockHandlerManager = mockHandlerManager;
        this.mockTaskManager = mockTaskManager;
        this.idGenerator = idGenerator;

    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<Object> executeMock(final HttpServletRequest request) {

        return this.mockHandlerManager.mock(Context.from(request, this.mockTaskManager, this.idGenerator));
    }

    private TempData executeA(final HttpServletRequest request) throws IOException {
        final String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        final String authType = request.getAuthType();
        final String contextPath = request.getContextPath();
        final Cookie[] cookies = request.getCookies();
        List<Cookie> cookieList = Collections.emptyList();
        if (null != cookies && cookies.length > 0) {
            cookieList = Arrays.asList();
        }
        final List<Map<String, String>> hearderList = this.getHeaders(request);
        final String method = request.getMethod();
        final String pathInfo = request.getPathInfo();
        final String pathTranslated = request.getPathTranslated();
        final String queryString = request.getQueryString();
        final String remoteUser = request.getRemoteUser();
        final String requestedSessionId = request.getRequestedSessionId();
        final String requestURI = request.getRequestURI();
        final StringBuffer requestURL = request.getRequestURL();
        final String servletPath = request.getServletPath();
        final List<Object> attributeList = this.getAttu(request);

        // 获取异常信息
        // 获取响应码，响应码跟异常信息有关。

        final TempData res = new TempData();
        res.setAuthType(authType)
                .setContextPath(contextPath)
                .setCookieList(cookieList)
                .setHearderList(hearderList)
                .setMethod(method)
                .setPathInfo(pathInfo)
                .setPathTranslated(pathTranslated)
                .setQueryString(queryString)
                .setRemoteUser(remoteUser)
                .setRequestedSessionId(requestedSessionId)
                .setRequestURI(requestURI)
                .setRequestURL(requestURL)
                .setServletPath(servletPath)
                .setAttributeList(attributeList)
                .setBody(JSON.parseObject(body));
        return res;
    }


    private List<Object> getAttu(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final List<Object> attributeList = new ArrayList<>();
        final Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            attributeList.add(request.getAttribute(attributeNames.nextElement()));
        }
        return attributeList;
    }

    private List<Map<String, String>> getHeaders(final HttpServletRequest request) {
        final List<Map<String, String>> list = new ArrayList<>();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String nextElement = headerNames.nextElement();
            final Enumeration<String> headers = request.getHeaders(nextElement);
            while (headers.hasMoreElements()) {
                final HashMap<String, String> map = new HashMap<>();
                map.put(nextElement, headers.nextElement());
                list.add(map);
            }
        }
        return list;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public class TempData {

        private String authType;
        private String contextPath;
        private List<Cookie> cookieList;
        private List<Map<String, String>> hearderList;
        private String method;
        private String pathInfo;
        private String pathTranslated;
        private String queryString;
        private String remoteUser;
        private String requestedSessionId;
        private String requestURI;
        private StringBuffer requestURL;
        private String servletPath;
        private List<Object> attributeList;
        private Object body;

    }

}