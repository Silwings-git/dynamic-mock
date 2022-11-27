package top.silwings.core.handler.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import top.silwings.core.common.Identity;
import top.silwings.core.config.DynamicMockContext;
import top.silwings.core.config.MockTaskLogProperties;
import top.silwings.core.event.MockTaskEndEvent;
import top.silwings.core.event.MockTaskStartEvent;
import top.silwings.core.model.MockTaskLogDto;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @ClassName MockTask
 * @Description
 * @Author Silwings
 * @Date 2022/11/13 10:07
 * @Since
 **/
@Slf4j
@Getter
@Builder
public class MockTask implements Runnable {

    /**
     * 处理器id
     */
    private final Identity handlerId;

    /**
     * 任务名称
     */
    private final String name;

    /**
     * 执行条件表述
     */
    private final List<String> support;

    /**
     * 请求地址
     */
    private final String requestUrl;

    /**
     * 请求方式
     */
    private final HttpMethod httpMethod;

    /**
     * 请求头
     */
    private final HttpHeaders headers;

    /**
     * 请求体
     */
    private final Object body;

    /**
     * uri参数
     */
    private final Map<String, ?> uriVariables;

    /**
     * cron
     */
    private final String cron;

    /**
     * 剩余执行次数
     */
    private final int numberOfExecute;

    /**
     * 承载该任务的定时任务
     */
    @Setter
    private AutoCancelTask autoCancelTask;

    private static Function<ClientResponse, Mono<ResponseInfo>> getResponseInfo() {
        return res -> {

            final MockTaskLogProperties mockTaskLogProperties = DynamicMockContext.getInstance().getMockTaskLogProperties();

            if (!mockTaskLogProperties.isLogResponse()) {
                return Mono.just(ResponseInfo.builder().build());
            }

            final ResponseInfo.ResponseInfoBuilder infoBuilder = ResponseInfo.builder();

            if (mockTaskLogProperties.isLogHttpStatus()) {
                infoBuilder.httpStatus(res.rawStatusCode());
            }

            if (mockTaskLogProperties.isLogResponseHeaders()) {
                infoBuilder.headers(res.headers().asHttpHeaders());
            }

            return res.bodyToMono(String.class)
                    .map(body -> {
                        if (mockTaskLogProperties.isLogResponseBody()) {
                            infoBuilder.body(body);
                        }
                        return infoBuilder.build();
                    });
        };
    }

    private static Consumer<ResponseInfo> publishEndEvent(final MockTaskStartEvent startEvent) {

        if (!DynamicMockContext.getInstance().getMockTaskLogProperties().isEnableLog()) {
            return responseInfo -> {
            };
        }

        return responseInfo -> {
            final MockTaskLogDto mockTaskLog = startEvent.getMockTaskLog();
            mockTaskLog.setResponseInfo(JsonUtils.toJSONString(responseInfo, JsonInclude.Include.NON_NULL));
            mockTaskLog.setTiming(System.currentTimeMillis() - mockTaskLog.getRequestTime().getTime());
            final MockTaskEndEvent endEvent = MockTaskEndEvent.of(startEvent.getSource(), mockTaskLog);
            DynamicMockContext.getInstance().getApplicationEventPublisher().publishEvent(endEvent);
        };
    }

    private static MockTaskStartEvent publishStartEvent(final MockTask mockTask) {

        final MockTaskLogProperties mockTaskLogProperties = DynamicMockContext.getInstance().getMockTaskLogProperties();

        if (!mockTaskLogProperties.isEnableLog()) {
            return MockTaskStartEvent.of(mockTask, null);
        }

        final MockTaskLogDto taskLog = MockTaskLogDto.builder()
                .taskCode(ConvertUtils.getNoNullOrDefault(mockTask.getAutoCancelTask(), null, AutoCancelTask::getTaskCode))
                .handlerId(mockTask.getHandlerId())
                .name(mockTask.getName())
                .registrationTime(ConvertUtils.getNoNullOrDefault(mockTask.getAutoCancelTask(), null, at -> new Date(at.getRegistrationTime())))
                .requestInfo(mockTaskLogProperties.isLogRequestInfo() ? JsonUtils.toJSONString(RequestInfo.from(mockTask), JsonInclude.Include.NON_NULL) : "{}")
                .requestTime(new Date())
                .build();

        final MockTaskStartEvent event = MockTaskStartEvent.of(mockTask, taskLog);

        DynamicMockContext.getInstance().getApplicationEventPublisher().publishEvent(event);

        return event;
    }

    @Override
    public void run() {
        try {
            this.sendRequest();
        } catch (Exception e) {
            log.error("MockTask run err.", e);
        }
    }

    protected void sendRequest() {

        final MockTaskStartEvent startEvent = publishStartEvent(this);

        DynamicMockContext.getInstance()
                .getWebClient()
                .method(this.httpMethod)
                .uri(this.getActualRequestUrl(this.requestUrl), this.uriVariables)
                .headers(h -> h.addAll(this.headers))
                .bodyValue(this.body)
                .exchangeToMono(getResponseInfo())
                .subscribe(publishEndEvent(startEvent));
    }

    private String getActualRequestUrl(final String requestUrl) {
        return requestUrl.startsWith("http://") || requestUrl.startsWith("https://") ? requestUrl : "http://" + requestUrl;
    }

    @Getter
    @Builder
    public static class RequestInfo {
        /**
         * 请求地址
         */
        private final String requestUrl;

        /**
         * 请求方式
         */
        private final HttpMethod httpMethod;

        /**
         * 请求头
         */
        private final HttpHeaders headers;

        /**
         * 请求体
         */
        private final Object body;

        /**
         * uri参数
         */
        private final Map<String, ?> uriVariables;

        public static RequestInfo from(final MockTask mockTask) {
            return RequestInfo.builder()
                    .requestUrl(mockTask.getRequestUrl())
                    .httpMethod(mockTask.getHttpMethod())
                    .headers(mockTask.getHeaders())
                    .body(mockTask.getBody())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseInfo {

        /**
         * 响应头
         */
        private final HttpHeaders headers;
        /**
         * 响应体
         */
        private final String body;
        /**
         * 状态码
         */
        private final Integer httpStatus;

    }

}