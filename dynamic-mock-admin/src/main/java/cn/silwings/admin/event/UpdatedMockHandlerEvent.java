package cn.silwings.admin.event;

import lombok.Getter;
import cn.silwings.core.common.Identity;

/**
 * @ClassName UpdatedMockHandlerEvent
 * @Description
 * @Author Silwings
 * @Date 2023/8/10 11:41
 * @Since
 **/
@Getter
public class UpdatedMockHandlerEvent extends MockHandlerAdminEvent {

    public UpdatedMockHandlerEvent(final Object source, final Identity handlerId) {
        super(source, handlerId);
    }

    public static UpdatedMockHandlerEvent of(final Object source, final Identity handlerId) {
        return new UpdatedMockHandlerEvent(source, handlerId);
    }
}