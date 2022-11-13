package top.silwings.core.handler.task;

import lombok.Getter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.DelayQueue;

/**
 * @ClassName MockTaskManager
 * @Description
 * @Author Silwings
 * @Date 2022/11/13 10:00
 * @Since
 **/
@Component
public class MockTaskManager {

    private final Map<String, WeakReference<MockTask>> mockTaskCache;

    private final DelayQueue<MockTask> taskDelayQueue;

    @Getter
    private final RestTemplate restTemplate;

    public MockTaskManager(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.mockTaskCache = new WeakHashMap<>();
        this.taskDelayQueue = new DelayQueue<>();
    }

    public void registerAsyncTask(final MockTask mockTask) {
        synchronized (this) {
            taskDelayQueue.add(mockTask);
            this.mockTaskCache.put(mockTask.getTaskId(), new WeakReference<>(mockTask));
        }
    }

    public void unregisterTask(final String taskId) {
        synchronized (this) {
            final WeakReference<MockTask> weakReference = this.mockTaskCache.get(taskId);
            if (null != weakReference) {
                final MockTask mockTask = weakReference.get();
                if (null != mockTask) {
                    mockTask.cancelTask();
                }
                this.mockTaskCache.remove(taskId);
            }
        }
    }

    public void unregisterAll() {
        synchronized (this) {
            this.mockTaskCache.values().forEach(ref -> {
                if (null != ref) {
                    final MockTask task = ref.get();
                    if (null != task) {
                        task.cancelTask();
                    }
                }
            });
        }
    }

    @Async("httpTaskScheduler")
    @Scheduled(cron = "* * * * * ?")
    public void execute() {

        final MockTask task = this.pollNextTask();

        if (null != task) {
            task.sendHttpRequest(this);
        }
    }

    private MockTask pollNextTask() {
        return this.taskDelayQueue.poll();
    }

    /**
     * 执行一次任务,并返回剩余可执行次数
     *
     * @param mockTask
     * @return
     */
    public void executeTask(final MockTask mockTask) {
        mockTask.sendHttpRequest(this);
    }

}