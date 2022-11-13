package top.silwings.core.handler.task;

import lombok.Builder;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.support.CronExpression;
import top.silwings.core.converter.HttpHeaderConverter;
import top.silwings.core.converter.UriVariableConvertor;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.AbstractMockSupport;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.tree.NodeInterpreter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName MockResponse
 * @Description Mock响应
 * @Author Silwings
 * @Date 2022/11/10 22:17
 * @Since
 **/
@Builder
public class MockTaskInfo extends AbstractMockSupport {

    private static final String MOCK_TASK_ID_PREFIX = "Task#";

    private final String name;

    private final List<NodeInterpreter> supportInterpreterList;

    private final int delayTime;

    private final boolean async;

    private final String cron;

    private final int numberOfExecute;

    private final NodeInterpreter mockTaskInterpreter;

    @Override
    protected List<NodeInterpreter> getSupportInterpreterList() {
        return this.supportInterpreterList;
    }

    public boolean isAsync() {
        return this.async;
    }

    public boolean isSync() {
        return !this.async;
    }

    public MockTask getMockTask(final Context context) {

        final Object interpret = this.mockTaskInterpreter.interpret(context);

        if (!(interpret instanceof Map)) {
            throw new DynamicMockException();
        }

        final Map<?, ?> map = (Map<?, ?>) interpret;

        return MockTask.builder()
                .taskId(MOCK_TASK_ID_PREFIX.concat(context.getIdGenerator().generateId().toString()))
                .requestUrl(String.valueOf(map.get("requestUrl")))
                .httpMethod(HttpMethod.valueOf(String.valueOf(map.get("httpMethod")).toUpperCase()))
                .headers(HttpHeaderConverter.from(map.get("headers")))
                .body(map.get("body"))
                .uriVariables(UriVariableConvertor.from(map.get("uriVariables")))
                .cronExpression(CronExpression.parse(this.cron))
                .numberOfExecute(new AtomicInteger(this.numberOfExecute))
                .build()
                .init();
    }

}