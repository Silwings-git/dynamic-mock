package cn.silwings.core.config;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;
import org.springframework.web.reactive.function.client.WebClient;
import cn.silwings.core.handler.task.MockTaskManager;
import cn.silwings.core.interpreter.dynamic_expression.DynamicExpressionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionExpressionFactory;
import cn.silwings.core.interpreter.json.JsonTreeParser;

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

    private final JsonTreeParser jsonTreeParser;

    private final DynamicExpressionFactory dynamicExpressionFactory;

    private final FunctionExpressionFactory functionExpressionFactory;

    private final WebClient webClient;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final MockTaskLogProperties mockTaskLogProperties;

    public DynamicMockContext(final MockTaskManager mockTaskManager, final IdGenerator idGenerator, final JsonTreeParser jsonTreeParser, final DynamicExpressionFactory dynamicExpressionFactory, final FunctionExpressionFactory functionExpressionFactory, final WebClient webClient, final ApplicationEventPublisher applicationEventPublisher, final MockTaskLogProperties mockTaskLogProperties) {
        this.mockTaskManager = mockTaskManager;
        this.idGenerator = idGenerator;
        this.jsonTreeParser = jsonTreeParser;
        this.dynamicExpressionFactory = dynamicExpressionFactory;
        this.functionExpressionFactory = functionExpressionFactory;
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