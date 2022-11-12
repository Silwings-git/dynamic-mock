package top.silwings.core.handler;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MockTask
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 17:36
 * @Since
 **/
@Builder
public class MockTask {

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