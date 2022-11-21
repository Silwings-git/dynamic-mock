package top.silwings.admin.model;

import lombok.Builder;
import lombok.Getter;
import top.silwings.admin.repository.db.mysql.po.ProjectPo;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.ConvertUtils;

/**
 * @ClassName Project
 * @Description 项目信息
 * @Author Silwings
 * @Date 2022/11/20 13:56
 * @Since
 **/
@Getter
@Builder
public class Project {

    private Identity projectId;

    private String projectName;

    private String baseUri;

    public static Project from(final ProjectPo projectPo) {

        return Project.builder()
                .projectId(Identity.from(projectPo.getProjectId()))
                .projectName(projectPo.getProjectName())
                .baseUri(projectPo.getBaseUri())
                .build();
    }

    public ProjectPo toProject() {
        final ProjectPo projectPo = new ProjectPo();
        projectPo.setProjectId(ConvertUtils.getNoNullOrDefault(this.projectId, null, Identity::intValue));
        projectPo.setProjectName(this.projectName);
        projectPo.setBaseUri(this.baseUri);
        return projectPo;
    }

}