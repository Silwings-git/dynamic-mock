package top.silwings.admin.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import top.silwings.admin.model.User;

/**
 * @ClassName DeleteUserEvent
 * @Description 删除用户事件
 * @Author Silwings
 * @Date 2022/11/20 16:03
 * @Since
 **/
@Getter
public class DeleteUserEvent extends ApplicationEvent {

    private final User user;

    private DeleteUserEvent(final Object source, final User user) {
        super(source);
        this.user = user;
    }

    public static DeleteUserEvent of(final Object source, final User user) {
        return new DeleteUserEvent(source, user);
    }

}