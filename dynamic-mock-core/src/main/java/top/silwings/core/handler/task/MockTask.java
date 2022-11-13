package top.silwings.core.handler.task;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
public class MockTask implements Delayed {

    /**
     * 唯一标识
     */
    private final String taskId;

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
    private final CronExpression cronExpression;

    /**
     * 剩余执行次数
     * 等于-1时表示没有次数限制
     */
    private final AtomicInteger numberOfExecute;

    public long getNextRunTime() {
        final LocalDateTime nextTime = this.cronExpression.next(LocalDateTime.now());
        if (null != nextTime) {
            return nextTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } else {
            this.cancelTask();
            return System.currentTimeMillis();
        }
    }

    public void cancelTask() {
        this.numberOfExecute.set(0);
    }

    public int getNumberOfExecute() {
        return this.numberOfExecute.get();
    }

    @Override
    public long getDelay(final TimeUnit unit) {
        return unit.convert(this.getNextRunTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(final Delayed other) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), other.getDelay(TimeUnit.MILLISECONDS));
    }

    protected void sendHttpRequest(final MockTaskManager mockTaskManager) {

        if (this.getNumberOfExecute() == 0) {
            return;
        }

        if (this.getNumberOfExecute() > 0) {
            final int num = this.numberOfExecute.decrementAndGet();
            if (num > 0) {
                mockTaskManager.registerAsyncTask(this);
            }
        }

        final RestTemplate restTemplate = mockTaskManager.getRestTemplate();

        final RequestEntity<Object> request =
                RequestEntity
                        .method(this.getHttpMethod(), this.getRequestUrl(), this.getUriVariables())
                        .headers(this.getHeaders())
                        .body(this.getBody());

        log.info("MockTask request info:{}", JSON.toJSONString(request));

        final ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        log.info("MockTask request result:{}", JSON.toJSONString(response));
    }

}