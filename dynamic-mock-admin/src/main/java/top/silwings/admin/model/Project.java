package top.silwings.admin.model;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import top.silwings.admin.common.enums.ProjectUserType;
import top.silwings.admin.repository.db.mysql.po.ProjectMockHandlerPo;
import top.silwings.admin.repository.db.mysql.po.ProjectPo;
import top.silwings.admin.repository.db.mysql.po.ProjectUserPo;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.ConvertUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    private List<ProjectMockHandler> projectMockHandlerList;

    public static Project from(final ProjectPo projectPo,final List<ProjectMockHandlerPo> projectMockHandlerPoList) {

        return Project.builder()
                .projectId(Identity.from(projectPo.getProjectId()))
                .projectName(projectPo.getProjectName())
                .baseUri(projectPo.getBaseUri())
                .projectMockHandlerList(projectMockHandlerPoList.stream().map(ProjectMockHandler::form).collect(Collectors.toList()))
                .build();
    }

    public ProjectPo toProject() {
        final ProjectPo projectPo = new ProjectPo();
        projectPo.setProjectId(ConvertUtils.getNoNullOrDefault(this.projectId, null, Identity::longValue));
        projectPo.setProjectName(this.projectName);
        projectPo.setBaseUri(this.baseUri);
        return projectPo;
    }

}