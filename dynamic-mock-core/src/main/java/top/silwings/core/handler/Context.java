package top.silwings.core.handler;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.util.IdGenerator;
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

    private final HandlerContext handlerContext;

    private final MockTaskManager mockTaskManager;

    private final IdGenerator idGenerator;

    public HandlerContext getHandlerContext() {
        return handlerContext;
    }

    public static Context from(final HttpServletRequest request, final MockTaskManager mockTaskManager, final IdGenerator idGenerator) {
        return Context.builder()
                .requestURI(request.getRequestURI())
                .httpMethod(HttpMethod.valueOf(request.getMethod()))
                .handlerContext(HandlerContext.from(request))
                .mockTaskManager(mockTaskManager)
                .idGenerator(idGenerator)
                .build();
    }

}