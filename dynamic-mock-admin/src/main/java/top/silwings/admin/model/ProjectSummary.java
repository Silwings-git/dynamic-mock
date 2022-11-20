package top.silwings.admin.model;

import lombok.Builder;
import lombok.Getter;
import top.silwings.admin.repository.db.mysql.po.ProjectPo;
import top.silwings.core.common.Identity;

/**
 * @ClassName ProjectSummary
 * @Description
 * @Author Silwings
 * @Date 2022/11/20 15:38
 * @Since
 **/
@Getter
@Builder
public class ProjectSummary {

    private Identity projectId;

    private String projectName;

    private String baseUri;

    public static ProjectSummary from(final ProjectPo projectPo) {

        return ProjectSummary.builder()
                .projectId(Identity.from(projectPo.getProjectId()))
                .projectName(projectPo.getProjectName())
                .baseUri(projectPo.getBaseUri())
                .build();
    }

}