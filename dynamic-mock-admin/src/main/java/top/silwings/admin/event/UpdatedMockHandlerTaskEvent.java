package top.silwings.admin.event;

import top.silwings.core.common.Identity;

/**
 * @ClassName UpdatedMockHandlerTaskEvent
 * @Description
 * @Author Silwings
 * @Date 2023/8/13 12:18
 * @Since
 **/
public class UpdatedMockHandlerTaskEvent extends UpdatedMockHandlerEvent{
    public UpdatedMockHandlerTaskEvent(final Object source, final Identity handlerId) {
        super(source, handlerId);
    }

    public static UpdatedMockHandlerTaskEvent of(final Object source, final Identity handlerId) {
        return new UpdatedMockHandlerTaskEvent(source, handlerId);
    }
}