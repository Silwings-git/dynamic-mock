package top.silwings.core.handler.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName MockTaskManager
 * @Description
 * @Author Silwings
 * @Date 2022/11/13 10:00
 * @Since
 **/
@Component
public class MockTaskManager {

    private final Map<String, WeakReference<AutoCancelTask>> taskCache;

    private final ThreadPoolTaskScheduler mockTaskThreadPool;

    public MockTaskManager(final ThreadPoolTaskScheduler mockTaskThreadPool) {
        this.mockTaskThreadPool = mockTaskThreadPool;
        this.taskCache = new WeakHashMap<>();
    }

    public void registerAsyncTask(final MockTask mockTask) {
        synchronized (this) {
            final AutoCancelTask autoCancelTask = AutoCancelTask.builder()
                    .taskId(mockTask.getTaskId())
                    .cron(mockTask.getCron())
                    .numberOfExecute(new AtomicInteger(mockTask.getNumberOfExecute()))
                    .task(mockTask)
                    .build()
                    // 执行注册
                    .schedule(this.mockTaskThreadPool);
            this.taskCache.put(autoCancelTask.getTaskId(), new WeakReference<>(autoCancelTask));
        }
    }

    public void unregisterTask(final String taskId, final boolean mayInterruptIfRunning) {
        synchronized (this) {
            final WeakReference<AutoCancelTask> weakReference = this.taskCache.get(taskId);
            if (null != weakReference) {
                final AutoCancelTask mockTask = weakReference.get();
                if (null != mockTask) {
                    mockTask.cancel(mayInterruptIfRunning);
                }
                this.taskCache.remove(taskId);
            }
        }
    }

    public void unregisterAll(final boolean mayInterruptIfRunning) {
        synchronized (this) {
            this.taskCache.values().forEach(ref -> {
                if (null != ref) {
                    final AutoCancelTask task = ref.get();
                    if (null != task) {
                        task.cancel(mayInterruptIfRunning);
                    }
                }
            });
        }
    }

}