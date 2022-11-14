package top.silwings.core.handler.task;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

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
        this.sendRequest(this.asyncRestTemplate);
    }

    protected void sendRequest(final AsyncRestTemplate asyncRestTemplate) {

        final RequestEntity<Object> request =
                RequestEntity
                        .method(this.getHttpMethod(), this.getRequestUrl(), this.getUriVariables())
                        .headers(this.getHeaders())
                        .body(this.getBody());

        log.debug("MockTask {} request. requestUrl:{} , method:{} ,headers: {} , uriVariables: {} , body:{}",
                this.getTaskId(),
                this.getRequestUrl(),
                this.getHttpMethod(),
                JSON.toJSONString(this.getHeaders()),
                JSON.toJSONString(this.getUriVariables()),
                JSON.toJSONString(this.getRequestUrl()));

        final ListenableFuture<ResponseEntity<String>> future = asyncRestTemplate.exchange(this.getRequestUrl(), this.getHttpMethod(), request, String.class, this.getUriVariables());

        future.addCallback(result -> log.debug("HttpTask {} 执行 {} 请求成功.响应信息: {}", this.getName(), this.getHttpMethod(), result)
                , ex -> log.error("HttpTask {} 执行 {} 请求失败. 错误信息: {}", this.getName(), this.getHttpMethod(), ex.getMessage()));

    }

}