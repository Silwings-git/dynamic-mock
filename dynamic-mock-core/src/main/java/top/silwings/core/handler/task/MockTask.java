package top.silwings.core.handler.task;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import top.silwings.core.utils.JsonUtils;

import java.util.Map;

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
     * 唯一标识
     */
    private final String taskId;

    /**
     * 任务名称
     */
    private final String name;

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
     * http客户端
     */
    private final AsyncRestTemplate asyncRestTemplate;

    @Override
    public void run() {
        try {
            this.sendRequest(this.asyncRestTemplate);
        } catch (Exception e) {
            log.error("MockTask run err.", e);
        }
    }

    protected void sendRequest(final AsyncRestTemplate asyncRestTemplate) {

        final String actualRequestUrl = this.getActualRequestUrl(this.getRequestUrl());

        final HttpEntity<Object> httpEntity = new HttpEntity<>(this.getBody(), this.getHeaders());

        log.info("MockTask {} request. requestUrl:{} , method:{} ,headers: {} , uriVariables: {} , body:{}",
                this.getTaskId(),
                actualRequestUrl,
                this.getHttpMethod(),
                JsonUtils.toJSONString(this.getHeaders()),
                JsonUtils.toJSONString(this.getUriVariables()),
                JsonUtils.toJSONString(this.getBody()));

        final ListenableFuture<ResponseEntity<String>> future = asyncRestTemplate.exchange(actualRequestUrl, this.getHttpMethod(), httpEntity, String.class, this.getUriVariables());

        future.addCallback(result -> log.debug("HttpTask {} 执行 {} 请求成功.响应信息: {}", this.getName(), this.getHttpMethod(), result)
                , ex -> log.error("HttpTask {} 执行 {} 请求失败. 错误信息: {}", this.getName(), this.getHttpMethod(), ex.getMessage()));

    }

    private String getActualRequestUrl(final String requestUrl) {
        return requestUrl.startsWith("http://") ? requestUrl : "http://" + requestUrl;
    }

}