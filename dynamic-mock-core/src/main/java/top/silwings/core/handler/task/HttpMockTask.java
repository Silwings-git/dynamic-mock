package top.silwings.core.handler.task;

import top.silwings.core.event.MockTaskEvent;

/**
 * @ClassName HttpMockTask
 * @Description
 * @Author Silwings
 * @Date 2023/1/4 23:06
 * @Since
 **/
public interface HttpMockTask<T extends MockTaskEvent, R> extends Task {

    T publishStartEvent();

    void sendRequest();

    R publishEndEvent(T t);

}