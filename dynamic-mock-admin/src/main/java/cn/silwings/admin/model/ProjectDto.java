package cn.silwings.admin.model;

import lombok.Builder;
import lombok.Getter;
import cn.silwings.admin.repository.po.ProjectPo;
import cn.silwings.core.common.Identity;
import cn.silwings.core.utils.ConvertUtils;

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
                .baseUri(ConvertUtils.getNoBlankOrDefault(projectPo.getBaseUri(), ""))
                .build();
    }

}