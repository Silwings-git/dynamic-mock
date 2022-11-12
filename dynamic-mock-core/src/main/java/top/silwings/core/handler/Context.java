package top.silwings.core.handler;

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
public class Context {

    /**
     * 请求地址
     */
    private final String requestURI;

    /**
     * 请求方法
     */
    private final HttpMethod httpMethod;

    private final HandlerContext handlerContext;

    public HandlerContext getHandlerContext() {
        return handlerContext;
    }

    public static Context from(final HttpServletRequest request) {
        // TODO_Silwings: 2022/11/10 基于request构建上下文信息
        return Context.builder()
                .requestURI(request.getRequestURI())
                .httpMethod(HttpMethod.valueOf(request.getMethod()))
                .handlerContext(new HandlerContext())
                .build();
    }

}