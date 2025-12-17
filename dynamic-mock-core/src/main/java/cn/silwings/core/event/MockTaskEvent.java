package cn.silwings.core.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import cn.silwings.core.model.MockTaskLogDto;

/**
 * @ClassName MockTaskRequestEvent
 * @Description Mock Task结束事件
 * @Author Silwings
 * @Date 2022/11/23 23:52
 * @Since
 **/
@Getter
public class MockTaskEvent extends ApplicationEvent {

    private final MockTaskLogDto mockTaskLog;

    public MockTaskEvent(final Object source, final MockTaskLogDto mockTaskLog) {
        super(source);
        this.mockTaskLog = mockTaskLog;
    }

}