package top.silwings.admin.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import top.silwings.core.common.Identity;

/**
 * @ClassName CreateMockHandlerEvent
 * @Description 创建mock处理器事件
 * @Author Silwings
 * @Date 2022/11/20 16:54
 * @Since
 **/
@Getter
public class SaveMockHandlerEvent extends ApplicationEvent {

    private final Identity handlerId;

    private final Identity projectId;


    public SaveMockHandlerEvent(final Object source, final Identity handlerId, final Identity projectId) {
        super(source);
        this.handlerId = handlerId;
        this.projectId = projectId;
    }

    public static SaveMockHandlerEvent of(final Object source, final Identity handlerId, final Identity projectId) {
        return new SaveMockHandlerEvent(source, handlerId, projectId);
    }

}