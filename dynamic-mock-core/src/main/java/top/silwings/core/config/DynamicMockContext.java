package top.silwings.core.config;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;
import org.springframework.web.reactive.function.client.WebClient;
import top.silwings.core.handler.JsonNodeParser;
import top.silwings.core.handler.task.MockTaskManager;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionDynamicValueFactory;

/**
 * @ClassName DynamicMockContext
 * @Description 全局上下文
 * @Author Silwings
 * @Date 2022/11/23 22:43
 * @Since
 **/
@Getter
@Component
public class DynamicMockContext implements InitializingBean {

    private static DynamicMockContext DYNAMIC_MOCK_CONTEXT = null;

    private final MockTaskManager mockTaskManager;

    private final IdGenerator idGenerator;

    private final JsonNodeParser jsonNodeParser;

    private final DynamicValueFactory dynamicValueFactory;

    private final FunctionDynamicValueFactory functionDynamicValueFactory;

    private final WebClient webClient;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final MockTaskLogProperties mockTaskLogProperties;

    public DynamicMockContext(final MockTaskManager mockTaskManager, final IdGenerator idGenerator, final JsonNodeParser jsonNodeParser, final DynamicValueFactory dynamicValueFactory, final FunctionDynamicValueFactory functionDynamicValueFactory, final WebClient webClient, final ApplicationEventPublisher applicationEventPublisher, final MockTaskLogProperties mockTaskLogProperties) {
        this.mockTaskManager = mockTaskManager;
        this.idGenerator = idGenerator;
        this.jsonNodeParser = jsonNodeParser;
        this.dynamicValueFactory = dynamicValueFactory;
        this.functionDynamicValueFactory = functionDynamicValueFactory;
        this.webClient = webClient;
        this.applicationEventPublisher = applicationEventPublisher;
        this.mockTaskLogProperties = mockTaskLogProperties;
    }

    public static DynamicMockContext getInstance() {
        return DYNAMIC_MOCK_CONTEXT;
    }

    @Override
    public void afterPropertiesSet() {
        DYNAMIC_MOCK_CONTEXT = this;
    }

}