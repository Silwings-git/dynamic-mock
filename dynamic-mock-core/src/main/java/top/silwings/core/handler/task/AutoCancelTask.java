package top.silwings.core.handler.task;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import top.silwings.core.common.Identity;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName MockTask
 * @Description
 * @Author Silwings
 * @Date 2022/11/14 21:07
 * @Since
 **/
@Slf4j
@Getter
@Builder
public class AutoCancelTask implements Runnable {

    /**
     * 唯一标识
     */
    private final String taskCode;

    private final Identity handlerId;

    private final String cron;

    private final AtomicInteger numberOfExecute;
    private final Runnable task;

    /**
     * 任务原始信息
     */
    private final String taskJson;

    /**
     * 任务注册时间
     */
    private final long registrationTime;

    private ScheduledFuture<?> future;

    @Override
    public void run() {
        try {
            this.task.run();
        } catch (Exception e) {
            log.error("Task execution failed.", e);
        } finally {
            if (this.numberOfExecute.decrementAndGet() <= 0) {
                this.cancel(false);
            }
        }
    }

    public Trigger getTrigger() {
        return new CronTrigger(this.cron);
    }

    public boolean cancel(final boolean mayInterruptIfRunning) {
        if (null != this.future && !this.future.isCancelled()) {
            return this.future.cancel(mayInterruptIfRunning);
        }
        return false;
    }

    public AutoCancelTask schedule(final TaskScheduler scheduler) {
        this.future = scheduler.schedule(this, this.getTrigger());
        return this;
    }

}