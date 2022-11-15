package top.silwings.core.handler;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.util.IdGenerator;
import org.springframework.web.client.AsyncRestTemplate;
import top.silwings.core.handler.task.MockTaskManager;

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

    private final RequestContext requestContext;

    private final MockTaskManager mockTaskManager;

    private final IdGenerator idGenerator;

    private final JsonNodeParser jsonNodeParser;

    private final AsyncRestTemplate asyncRestTemplate;

    public RequestContext getRequestContext() {
        return requestContext;
    }

    public static Context from(final HttpServletRequest request, final MockTaskManager mockTaskManager, final IdGenerator idGenerator, final JsonNodeParser jsonNodeParser, final AsyncRestTemplate asyncRestTemplate) {
        return Context.builder()
                .requestURI(request.getRequestURI())
                .httpMethod(HttpMethod.valueOf(request.getMethod()))
                .requestContext(RequestContext.from(request))
                .mockTaskManager(mockTaskManager)
                .idGenerator(idGenerator)
                .jsonNodeParser(jsonNodeParser)
                .asyncRestTemplate(asyncRestTemplate)
                .build();
    }

}