package top.silwings.admin.events;

import org.springframework.context.ApplicationEvent;

/**
 * @ClassName DeleteEvent
 * @Description 删除事件
 * @Author Silwings
 * @Date 2022/11/20 16:19
 * @Since
 **/
public abstract class DeleteEvent extends ApplicationEvent {

    public DeleteEvent(final Object source) {
        super(source);
    }
}