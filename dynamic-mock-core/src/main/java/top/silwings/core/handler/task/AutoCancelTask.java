package top.silwings.core.handler.task;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Getter
@Builder
public class AutoCancelTask implements Runnable {

    /**
     * 唯一标识
     */
    private final String taskId;

    private final String cron;

    private final AtomicInteger numberOfExecute;

    private ScheduledFuture<?> future;

    private final Runnable task;

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