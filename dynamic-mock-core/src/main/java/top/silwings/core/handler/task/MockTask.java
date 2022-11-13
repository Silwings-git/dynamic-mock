package top.silwings.core.handler.task;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.support.CronExpression;

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
}