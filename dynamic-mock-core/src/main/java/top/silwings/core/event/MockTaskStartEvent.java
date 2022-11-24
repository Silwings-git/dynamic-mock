package top.silwings.core.event;

import top.silwings.core.model.MockTaskLogDto;

/**
 * @ClassName MockTaskRequestEvent
 * @Description Mock Task启动事件
 * @Author Silwings
 * @Date 2022/11/23 23:52
 * @Since
 **/
public class MockTaskStartEvent extends MockTaskEvent {

    public MockTaskStartEvent(final Object source, final MockTaskLogDto mockTaskLog) {
        super(source, mockTaskLog);
    }

    public static MockTaskStartEvent of(final Object source, final MockTaskLogDto mockTaskLog) {
        return new MockTaskStartEvent(source, mockTaskLog);
    }

    public static MockTaskStartEvent from(final Object source, final MockTaskLogDto taskLog) {
        return new MockTaskStartEvent(source, taskLog);
    }
}