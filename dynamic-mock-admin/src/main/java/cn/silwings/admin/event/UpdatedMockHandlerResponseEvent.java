package cn.silwings.admin.event;

import cn.silwings.core.common.Identity;

/**
 * @ClassName UpdatedMockHandlerResponseEvent
 * @Description
 * @Author Silwings
 * @Date 2023/8/13 12:16
 * @Since
 **/
public class UpdatedMockHandlerResponseEvent extends UpdatedMockHandlerEvent {
    public UpdatedMockHandlerResponseEvent(final Object source, final Identity handlerId) {
        super(source, handlerId);
    }

    public static UpdatedMockHandlerResponseEvent of(final Object source, final Identity handlerId) {
        return new UpdatedMockHandlerResponseEvent(source, handlerId);
    }

}