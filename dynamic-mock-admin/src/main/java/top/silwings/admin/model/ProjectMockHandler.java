package top.silwings.admin.model;

import lombok.Builder;
import lombok.Getter;
import top.silwings.admin.repository.db.mysql.po.ProjectMockHandlerPo;
import top.silwings.core.common.Identity;

/**
 * @ClassName ProjectMockHandler
 * @Description 项目与Mock处理器关联
 * @Author Silwings
 * @Date 2022/11/20 15:02
 * @Since
 **/
@Getter
@Builder
public class ProjectMockHandler {

    private Identity projectId;

    private Identity handlerId;

    public static ProjectMockHandler form(final ProjectMockHandlerPo projectMockHandlerPo) {
        return ProjectMockHandler.builder()
                .projectId(Identity.from(projectMockHandlerPo.getProjectId()))
                .handlerId(Identity.from(projectMockHandlerPo.getHandlerId()))
                .build();
    }
}