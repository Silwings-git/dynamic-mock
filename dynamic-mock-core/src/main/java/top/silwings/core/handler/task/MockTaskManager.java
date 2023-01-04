package top.silwings.core.handler.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import top.silwings.core.common.Identity;
import top.silwings.core.config.TaskSchedulerProperties;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @ClassName MockTaskManager
 * @Description
 * @Author Silwings
 * @Date 2022/11/13 10:00
 * @Since
 **/
@Slf4j
@Component
public class MockTaskManager implements DisposableBean {

    private final Map<String, WeakReference<AutoCancelCronTask>> taskPool;

    private final ThreadPoolTaskScheduler taskScheduler;

    private final TaskSchedulerProperties taskSchedulerProperties;

    public MockTaskManager(final ThreadPoolTaskScheduler taskScheduler, final TaskSchedulerProperties taskSchedulerProperties) {
        this.taskScheduler = taskScheduler;
        this.taskSchedulerProperties = taskSchedulerProperties;
        this.taskPool = new WeakHashMap<>();
    }

    public void registerAsyncTask(final MockTask mockTask) {

        synchronized (this) {

            // 限制taskPool容量
            this.limitCapacity();

            final AutoCancelCronTask autoCancelCronTask = AutoCancelCronTask.builder()
                    .handlerId(mockTask.getHandlerId())
                    .cron(mockTask.getCron())
                    .numberOfExecute(new AtomicInteger(mockTask.getNumberOfExecute()))
                    .task(mockTask)
                    .build()
                    // 执行注册
                    .schedule(this.taskScheduler);

            final RegistrationInfo registrationInfo = RegistrationInfo.builder()
                    .taskCode(this.buildTaskCode(mockTask.getHandlerId()))
                    .registrationTime(System.currentTimeMillis())
                    .build();

            autoCancelCronTask.setRegistrationInfo(registrationInfo);
            this.taskPool.put(registrationInfo.getTaskCode(), new WeakReference<>(autoCancelCronTask));
        }
    }

    /**
     * 限制taskPool的容量.
     * 如果超过设置的值,将取消最先注册的任务
     */
    private void limitCapacity() {

        int beyond = this.taskPool.size() - this.taskSchedulerProperties.getMaxTaskPoolSize();

        if (beyond > 0) {
            // 优先将剩余执行次数为0的清除
            this.taskPool.values().stream().map(WeakReference::get).filter(Objects::nonNull)
                    .filter(task -> task.getNumberOfExecute().get() == 0).forEach(task -> this.cancelTask(task, false));
            beyond = this.taskPool.size() - this.taskSchedulerProperties.getMaxTaskPoolSize();
        }

        if (beyond > 0) {
            this.taskPool.values().stream().map(WeakReference::get)
                    .filter(Objects::nonNull).sorted(Comparator.comparingLong(task -> task.getRegistrationInfo().getRegistrationTime()))
                    .limit(beyond + 1).forEach(task -> {
                        this.cancelTask(task, true);
                        log.error("存活任务数量超过任务池最大数量,任务:{}被自动关闭.", task.getRegistrationInfo().getTaskCode());
                    });
        }
    }

    public void unregisterByTaskCode(final Identity handlerId, final String taskCode, final Boolean mayInterruptIfRunning) {
        final WeakReference<AutoCancelCronTask> weakReference = this.taskPool.get(taskCode);
        if (null != weakReference) {
            final AutoCancelCronTask mockTask = weakReference.get();
            if (null != mockTask && mockTask.getHandlerId().equals(handlerId)) {
                this.cancelTask(mockTask, Boolean.TRUE.equals(mayInterruptIfRunning));
            }
        }
    }

    public void unregisterByHandlerIds(final List<Identity> handlerIdList, final Boolean mayInterruptIfRunning) {
        final List<AutoCancelCronTask> autoCancelCronTasks = this.query(handlerIdList);
        autoCancelCronTasks.forEach(task -> this.cancelTask(task, Boolean.TRUE.equals(mayInterruptIfRunning)));
    }

    private void cancelTask(final AutoCancelCronTask mockTask, final boolean mayInterruptIfRunning) {
        synchronized (this) {
            if (null != mockTask) {
                mockTask.cancel(mayInterruptIfRunning);
                this.taskPool.remove(mockTask.getRegistrationInfo().getTaskCode());
            }
        }
    }

    /**
     * 生成任务id.
     * 处理器id+'-'+时间戳+随机数
     *
     * @param handlerId Mock Handler id
     * @return 任务id
     */
    private String buildTaskCode(final Identity handlerId) {
        return handlerId.stringValue() + "-" + System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(1000);
    }

    public List<AutoCancelCronTask> query(final List<Identity> handlerIdList) {

        if (CollectionUtils.isEmpty(handlerIdList)) {
            return Collections.emptyList();
        }

        final Set<Map.Entry<String, WeakReference<AutoCancelCronTask>>> taskEntrySet = this.taskPool.entrySet();

        final List<AutoCancelCronTask> autoCancelCronTaskList = new ArrayList<>();

        for (final Identity handlerId : handlerIdList) {
            for (final Map.Entry<String, WeakReference<AutoCancelCronTask>> entry : taskEntrySet) {
                final String taskCode = entry.getKey();
                final WeakReference<AutoCancelCronTask> taskReference = entry.getValue();
                if (taskCode.startsWith(handlerId.stringValue() + "-") && null != taskReference) {
                    autoCancelCronTaskList.add(taskReference.get());
                }
            }
        }

        return autoCancelCronTaskList.stream()
                .filter(Objects::nonNull)
                .filter(task -> task.getNumberOfExecute().get() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public void destroy() {
        this.taskScheduler.destroy();
    }
}