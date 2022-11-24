package top.silwings.core.handler.task;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import top.silwings.core.common.Identity;
import top.silwings.core.config.TaskSchedulerProperties;
import top.silwings.core.utils.JsonUtils;

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
@Component
public class MockTaskManager implements DisposableBean {

    private final Map<String, WeakReference<AutoCancelTask>> taskPool;

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

            final AutoCancelTask autoCancelTask = AutoCancelTask.builder()
                    .handlerId(mockTask.getHandlerId())
                    .taskCode(this.buildTaskCode(mockTask.getHandlerId()))
                    .cron(mockTask.getCron())
                    .numberOfExecute(new AtomicInteger(mockTask.getNumberOfExecute()))
                    .task(mockTask)
                    .taskJson(JsonUtils.toJSONString(mockTask))
                    .registrationTime(System.currentTimeMillis())
                    .build()
                    // 执行注册
                    .schedule(this.taskScheduler);
            mockTask.setAutoCancelTask(autoCancelTask);
            this.taskPool.put(autoCancelTask.getTaskCode(), new WeakReference<>(autoCancelTask));
        }
    }

    /**
     * 限制taskPool的容量.
     * 如果超过设置的值,将取消最先注册的任务
     */
    private void limitCapacity() {
        final int beyond = this.taskPool.size() - this.taskSchedulerProperties.getMaxTaskPoolSize();
        if (beyond > 0) {
            this.taskPool.values().stream().map(WeakReference::get)
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingLong(AutoCancelTask::getRegistrationTime))
                    .limit(beyond + 1)
                    .forEach(task -> this.cancelTask(task, true));
        }
    }

    public void unregisterByTaskCode(final Identity handlerId, final String taskCode, final boolean mayInterruptIfRunning) {
        final WeakReference<AutoCancelTask> weakReference = this.taskPool.get(taskCode);
        if (null != weakReference) {
            final AutoCancelTask mockTask = weakReference.get();
            if (null != mockTask && mockTask.getHandlerId().equals(handlerId)) {
                this.cancelTask(mockTask, mayInterruptIfRunning);
            }
        }
    }

    public void unregisterByHandlerId(final Identity handlerId, final boolean mayInterruptIfRunning) {
        this.unregisterByHandlerIds(Collections.singletonList(handlerId), mayInterruptIfRunning);
    }

    public void unregisterByHandlerIds(final List<Identity> handlerIdList, final boolean mayInterruptIfRunning) {
        final List<AutoCancelTask> autoCancelTasks = this.query(handlerIdList);
        autoCancelTasks.forEach(task -> this.cancelTask(task, mayInterruptIfRunning));
    }

    private void cancelTask(final AutoCancelTask mockTask, final boolean mayInterruptIfRunning) {
        synchronized (this) {
            if (null != mockTask) {
                mockTask.cancel(mayInterruptIfRunning);
                this.taskPool.remove(mockTask.getTaskCode());
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

    public List<AutoCancelTask> query(final List<Identity> handlerIdList) {

        if (CollectionUtils.isEmpty(handlerIdList)) {
            return Collections.emptyList();
        }

        final Set<Map.Entry<String, WeakReference<AutoCancelTask>>> taskEntrySet = this.taskPool.entrySet();

        final List<AutoCancelTask> autoCancelTaskList = new ArrayList<>();

        for (final Identity handlerId : handlerIdList) {
            for (final Map.Entry<String, WeakReference<AutoCancelTask>> entry : taskEntrySet) {
                final String taskCode = entry.getKey();
                final WeakReference<AutoCancelTask> taskReference = entry.getValue();
                if (taskCode.startsWith(handlerId.stringValue() + "-") && null != taskReference) {
                    autoCancelTaskList.add(taskReference.get());
                }
            }
        }

        return autoCancelTaskList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void destroy() {
        this.taskScheduler.destroy();
    }
}