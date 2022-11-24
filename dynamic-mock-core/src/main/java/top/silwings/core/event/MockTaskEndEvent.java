package top.silwings.core.event;

import lombok.Getter;
import top.silwings.core.model.MockTaskLogDto;

/**
 * @ClassName MockTaskRequestEvent
 * @Description Mock Task结束事件
 * @Author Silwings
 * @Date 2022/11/23 23:52
 * @Since
 **/
@Getter
public class MockTaskEndEvent extends MockTaskEvent {

    public MockTaskEndEvent(final Object source, final MockTaskLogDto mockTaskLog) {
        super(source, mockTaskLog);
    }

    public static MockTaskEndEvent from(final Object source, final MockTaskLogDto mockTaskLog) {
        return new MockTaskEndEvent(source, mockTaskLog);
    }
}