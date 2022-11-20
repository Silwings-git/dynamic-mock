package top.silwings.core.handler;

import lombok.Getter;
import org.springframework.http.HttpMethod;

/**
 * @ClassName RequestInfo
 * @Description 请求信息
 * @Author Silwings
 * @Date 2022/11/10 21:56
 * @Since
 **/
@Getter
public class RequestInfo {

    /**
     * 请求地址
     */
    private final String requestUri;

    /**
     * 请求方法
     */
    private final HttpMethod httpMethod;

    public RequestInfo(final String requestUri, final HttpMethod httpMethod) {
        this.requestUri = requestUri;
        this.httpMethod = httpMethod;
    }

    public static RequestInfo from(final String requestUri, final HttpMethod httpMethod) {
        return new RequestInfo(requestUri, httpMethod);
    }

    public static RequestInfo from(final Context context) {

        final String requestUri = context.getRequestURI();
        final HttpMethod method = context.getHttpMethod();

        return new RequestInfo(requestUri, method);
    }

}