package top.silwings.core.handler.context;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName Context
 * @Description 全局信息
 * @Author Silwings
 * @Date 2022/10/28 17:32
 * @Since
 **/
@Getter
@Builder
public class MockHandlerContext {

    /**
     * 请求地址
     */
    private final String requestURI;

    /**
     * 请求方法
     */
    private final HttpMethod httpMethod;

    private final RequestContext requestContext;

    public static MockHandlerContext from(final HttpServletRequest request) {
        return MockHandlerContext.builder()
                .requestURI(request.getRequestURI())
                .httpMethod(HttpMethod.valueOf(request.getMethod()))
                .requestContext(RequestContext.from(request))
                .build();
    }

    public RequestContext getRequestContext() {
        return requestContext;
    }

}