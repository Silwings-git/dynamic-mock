package top.silwings.admin.events;

import lombok.Getter;
import top.silwings.core.common.Identity;

/**
 * @ClassName DeleteUserEvent
 * @Description 删除用户事件
 * @Author Silwings
 * @Date 2022/11/20 16:03
 * @Since
 **/
@Getter
public class DeleteMockHandlerEvent extends DeleteEvent {

    private final Identity handlerId;

    public DeleteMockHandlerEvent(final Object source, final Identity handlerId) {
        super(source);
        this.handlerId = handlerId;
    }

    public static DeleteMockHandlerEvent of(final Object source, final Identity handlerId) {
        return new DeleteMockHandlerEvent(source, handlerId);
    }

}