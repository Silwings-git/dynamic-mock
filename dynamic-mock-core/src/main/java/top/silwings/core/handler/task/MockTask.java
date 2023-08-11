package top.silwings.core.handler.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
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
public class MockTask implements HttpMockTask<MockTaskStartEvent, Consumer<MockTask.ResponseInfo>>, RegisterAware {

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

    private RegistrationInfo registrationInfo;

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

    @Override
    public void setRegistrationInfo(final RegistrationInfo registrationInfo) {
        this.registrationInfo = registrationInfo;
    }

    @Override
    public void run() {
        try {
            this.sendRequest();
        } catch (Exception e) {
            log.error("MockTask run err.", e);
        }
    }

    @Override
    public MockTaskStartEvent publishStartEvent() {

        final MockTaskLogProperties mockTaskLogProperties = DynamicMockContext.getInstance().getMockTaskLogProperties();

        if (!mockTaskLogProperties.isEnableLog()) {
            return MockTaskStartEvent.of(this, null);
        }

        final MockTaskLogDto taskLog = MockTaskLogDto.builder()
                .taskCode(this.registrationInfo.getTaskCode())
                .handlerId(this.getHandlerId())
                .name(this.getName())
                .registrationTime(new Date(this.registrationInfo.getRegistrationTime()))
                .requestInfo(mockTaskLogProperties.isLogRequestInfo() ? JsonUtils.toJSONString(RequestInfo.from(this), JsonInclude.Include.NON_NULL) : JsonUtils.EMPTY_JSON)
                .requestTime(new Date())
                .build();

        final MockTaskStartEvent event = MockTaskStartEvent.of(this, taskLog);

        DynamicMockContext.getInstance().getApplicationEventPublisher().publishEvent(event);

        return event;
    }

    @Override
    public Consumer<ResponseInfo> publishEndEvent(final MockTaskStartEvent mockTaskEvent) {

        if (!DynamicMockContext.getInstance().getMockTaskLogProperties().isEnableLog()) {
            return responseInfo -> {
            };
        }

        return responseInfo -> {
            final MockTaskLogDto mockTaskLog = mockTaskEvent.getMockTaskLog();
            mockTaskLog.setResponseInfo(JsonUtils.toJSONString(responseInfo, JsonInclude.Include.NON_NULL));
            mockTaskLog.setTiming(System.currentTimeMillis() - mockTaskLog.getRequestTime().getTime());
            final MockTaskEndEvent endEvent = MockTaskEndEvent.of(mockTaskEvent.getSource(), mockTaskLog);
            DynamicMockContext.getInstance().getApplicationEventPublisher().publishEvent(endEvent);
        };
    }

    @Override
    public void sendRequest() {

        final MockTaskStartEvent startEvent = this.publishStartEvent();

        DynamicMockContext.getInstance()
                .getWebClient()
                .method(this.httpMethod)
                .uri(this.getActualRequestUrl(this.requestUrl), this.uriVariables)
                .headers(h -> h.addAll(this.headers))
                .bodyValue(this.body)
                .exchangeToMono(getResponseInfo())
                .subscribe(this.publishEndEvent(startEvent));
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