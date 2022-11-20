package top.silwings.admin.repository;

import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.common.enums.ProjectUserType;
import top.silwings.admin.model.Project;
import top.silwings.admin.model.ProjectSummary;
import top.silwings.core.common.Identity;

/**
 * @ClassName ProjectRepository
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 19:40
 * @Since
 **/
public interface ProjectRepository {
    void create(Project project, final Identity author);

    void update(Identity projectId, Project project);

    Project find(Identity projectId, final boolean findUser, final boolean findHandler);

    void delete(Identity projectId);

    PageData<ProjectSummary> querySummary(String projectName, PageParam pageParam);

    void createProjectUser(Identity projectId, Identity userId, ProjectUserType type);

    void deleteProjectUserByUserId(Identity userId);

    void deleteProjectHandlerByHandlerId(Identity handlerId);
}