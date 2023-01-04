package top.silwings.core.handler.task;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.JsonUtils;

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
public class AutoCancelCronTask implements Runnable, RegisterAware {

    private final Identity handlerId;

    private final String cron;

    private final AtomicInteger numberOfExecute;

    private final Task task;

    private ScheduledFuture<?> future;

    private RegistrationInfo registrationInfo;

    @Override
    public void setRegistrationInfo(final RegistrationInfo registrationInfo) {
        this.registrationInfo = registrationInfo;
        if (this.task instanceof RegisterAware) {
            ((RegisterAware) this.task).setRegistrationInfo(registrationInfo);
        }
    }

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

    public AutoCancelCronTask schedule(final TaskScheduler scheduler) {
        this.future = scheduler.schedule(this, this.getTrigger());
        return this;
    }

    public String getTaskJson() {
        return JsonUtils.toJSONString(this.task);
    }

}