package top.silwings.admin.model;

import lombok.Builder;
import lombok.Getter;
import top.silwings.admin.repository.db.mysql.po.ProjectUserPo;
import top.silwings.core.common.Identity;

/**
 * @ClassName ProjectUser
 * @Description
 * @Author Silwings
 * @Date 2022/11/20 14:27
 * @Since
 **/
@Getter
@Builder
public class ProjectUser {

    private Identity projectId;

    private Identity userId;

    private Integer type;

    public ProjectUserPo toProjectUser() {
        final ProjectUserPo projectUserPo = new ProjectUserPo();
        projectUserPo.setProjectId(this.projectId.longValue());
        projectUserPo.setUserId(this.userId.longValue());
        projectUserPo.setType(this.type);
        return projectUserPo;
    }

    public static ProjectUser from(final ProjectUserPo projectUserPo) {
        return ProjectUser.builder()
                .projectId(Identity.from(projectUserPo.getProjectId()))
                .userId(Identity.from(projectUserPo.getUserId()))
                .type(projectUserPo.getType())
                .build();
    }
}