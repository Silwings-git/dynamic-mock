package top.silwings.core.handler.task;

/**
 * @ClassName TaskQueue
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 18:18
 * @Since
 **/
public class TaskQueue {

    public TaskQueue registerAsyncTask(final MockTaskInfo mockTask) {
        // TODO_Silwings: 2022/11/12 registerTask 注意Context的线程安全
        return this;
    }

}