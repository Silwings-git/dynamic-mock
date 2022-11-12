package top.silwings.core.handler.task;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import top.silwings.core.handler.Context;

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
public class MockTaskInfo {

    private final String name;

    private final List<String> supportList;

    private final int delayTime;

    private final boolean async;

    private final String cron;

    private final int numberOfExecute;

    private final Request request;

    public boolean support(final Context context) {
        return false;
    }

    public boolean isAsync() {
        return this.async;
    }

    public boolean isSync() {
        return !this.async;
    }

    @Getter
    @Builder
    private static class Request {
        private final String requestUrl;
        private final HttpMethod httpMethod;
        private final HttpHeaders headers;
        private final Object body;
        private final Map<String, ?> uriVariables;
    }

}