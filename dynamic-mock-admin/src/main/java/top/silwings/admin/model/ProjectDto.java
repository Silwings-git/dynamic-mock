package top.silwings.admin.model;

import lombok.Builder;
import lombok.Getter;
import top.silwings.admin.repository.po.ProjectPo;
import top.silwings.core.common.Identity;

/**
 * @ClassName Project
 * @Description 项目信息
 * @Author Silwings
 * @Date 2022/11/20 13:56
 * @Since
 **/
@Getter
@Builder
public class ProjectDto {

    private Identity projectId;

    private String projectName;

    private String baseUri;

    public static ProjectDto from(final ProjectPo projectPo) {

        return ProjectDto.builder()
                .projectId(Identity.from(projectPo.getProjectId()))
                .projectName(projectPo.getProjectName())
                .baseUri(projectPo.getBaseUri())
                .build();
    }

}