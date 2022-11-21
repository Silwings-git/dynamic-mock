package top.silwings.admin.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.events.DeleteProjectEvent;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.model.Project;
import top.silwings.admin.model.ProjectSummary;
import top.silwings.admin.repository.ProjectRepository;
import top.silwings.admin.service.ProjectService;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;

/**
 * @ClassName ProjectServiceImpl
 * @Description 项目管理
 * @Author Silwings
 * @Date 2022/11/20 13:54
 * @Since
 **/
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public ProjectServiceImpl(final ProjectRepository projectRepository, final ApplicationEventPublisher applicationEventPublisher) {
        this.projectRepository = projectRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void save(final Identity projectId, final String projectName, final String baseUri) {

        final Project newProject = Project.builder()
                .projectName(projectName)
                .baseUri(baseUri)
                .build();

        if (null == projectId) {

            this.projectRepository.create(newProject, UserHolder.getUserId());

        } else {

            final Project project = this.projectRepository.find(projectId, true, false);

            CheckUtils.isNotNull(project, () -> DynamicMockAdminException.from("Project does not exist."));

            this.projectRepository.update(projectId, newProject);
        }
    }

    @Override
    @Transactional
    public void delete(final Identity projectId) {

        final Project project = this.projectRepository.find(projectId, true, true);

        if (null == project) {
            return;
        }

        if (this.projectRepository.delete(projectId)) {
            this.applicationEventPublisher.publishEvent(DeleteProjectEvent.of(this, project));
        }

    }

    @Override
    public PageData<ProjectSummary> querySummary(final String projectName, final PageParam pageParam) {
        return this.projectRepository.querySummary(projectName, pageParam);
    }

}