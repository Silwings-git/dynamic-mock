package top.silwings.core.handler.task;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import top.silwings.core.handler.AbstractMockSupport;
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
public class MockTaskInfo extends AbstractMockSupport {

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

    @Getter
    @Builder
    private static class MockTask {
        private final String requestUrl;
        private final HttpMethod httpMethod;
        private final HttpHeaders headers;
        private final Object body;
        private final Map<String, ?> uriVariables;
    }

}