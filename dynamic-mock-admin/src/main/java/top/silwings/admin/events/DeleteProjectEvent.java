package top.silwings.admin.events;

import lombok.Getter;
import top.silwings.admin.model.Project;

/**
 * @ClassName DeleteUserEvent
 * @Description 删除用户事件
 * @Author Silwings
 * @Date 2022/11/20 16:03
 * @Since
 **/
@Getter
public class DeleteProjectEvent extends DeleteEvent {

    private final Project project;

    private DeleteProjectEvent(final Object source, final Project project) {
        super(source);
        this.project = project;
    }

    public static DeleteProjectEvent of(final Object source, final Project project) {
        return new DeleteProjectEvent(source, project);
    }

}