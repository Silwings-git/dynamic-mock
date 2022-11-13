package top.silwings.core.handler.task;

import org.springframework.stereotype.Component;

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

    private final MockTaskHandler mockTaskHandler;

    private final Map<String, WeakReference<MockTask>> mockTaskCache;

    private final DelayQueue<MockTask> taskDelayQueue;

    public MockTaskManager(final MockTaskHandler mockTaskHandler) {
        this.mockTaskHandler = mockTaskHandler;
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


    /**
     * 执行一次任务,并返回剩余可执行次数
     *
     * @param mockTask
     * @return
     */
    public int executeTask(final MockTask mockTask) {

        this.mockTaskHandler.execute(mockTask);

        return mockTask.getNumberOfExecute();
    }

}