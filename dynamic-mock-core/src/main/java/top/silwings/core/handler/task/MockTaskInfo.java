package top.silwings.core.handler.task;

import lombok.Builder;
import org.springframework.http.HttpMethod;
import top.silwings.core.common.Identity;
import top.silwings.core.converter.HttpHeaderConverter;
import top.silwings.core.converter.UriVariableConvertor;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.AbstractSupportAble;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.NodeInterpreter;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MockResponse
 * @Description Mock响应
 * @Author Silwings
 * @Date 2022/11/10 22:17
 * @Since
 **/
@Builder
public class MockTaskInfo extends AbstractSupportAble {

    private final String name;

    private final List<NodeInterpreter> supportInterpreterList;

    private final boolean async;

    private final String cron;

    private final int numberOfExecute;

    private final NodeInterpreter mockTaskInterpreter;

    private Identity handlerId;

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

    public MockTask getMockTask(final MockHandlerContext mockHandlerContext) {

        final Object interpret = this.mockTaskInterpreter.interpret(mockHandlerContext);

        if (!(interpret instanceof Map)) {
            throw new DynamicMockException("Task parsing failed: " + this.name);
        }

        final Map<?, ?> map = (Map<?, ?>) interpret;

        return MockTask.builder()
                .handlerId(this.handlerId)
                .name(this.name)
                .requestUrl(String.valueOf(map.get("requestUrl")))
                .httpMethod(HttpMethod.valueOf(String.valueOf(map.get("httpMethod")).toUpperCase()))
                .headers(HttpHeaderConverter.from(map.get("headers")))
                .body(map.get("body"))
                .uriVariables(UriVariableConvertor.from(map.get("uriVariables")))
                .cron(this.cron)
                .numberOfExecute(this.numberOfExecute)
                .build();
    }

}