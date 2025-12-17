package cn.silwings.admin.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import cn.silwings.core.common.Identity;

/**
 * @ClassName MockHandlerAdminEvent
 * @Description
 * @Author Silwings
 * @Date 2023/8/10 11:47
 * @Since
 **/
@Getter
public class MockHandlerAdminEvent extends ApplicationEvent {

    private final Identity handlerId;

    protected MockHandlerAdminEvent(final Object source, final Identity handlerId) {
        super(source);
        this.handlerId = handlerId;
    }
}