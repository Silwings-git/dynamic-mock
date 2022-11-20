package top.silwings.admin.service.impl;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.common.enums.ProjectUserType;
import top.silwings.admin.events.DeleteMockHandlerEvent;
import top.silwings.admin.events.DeleteProjectEvent;
import top.silwings.admin.events.DeleteUserEvent;
import top.silwings.admin.events.SaveMockHandlerEvent;
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
public class ProjectServiceImpl implements ProjectService, ApplicationListener<ApplicationEvent> {

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
            if (!UserHolder.isAdminUser()) {
                CheckUtils.isTrue(this.projectRepository.isProjectAdmin(projectId, UserHolder.getUserId()), () -> DynamicMockAdminException.from("Insufficient permissions."));
            }

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

        if (!UserHolder.isAdminUser()) {
            CheckUtils.isTrue(this.projectRepository.isProjectAdmin(projectId, UserHolder.getUserId()), () -> DynamicMockAdminException.from("Insufficient permissions."));
        }

        if (this.projectRepository.delete(projectId)) {
            this.applicationEventPublisher.publishEvent(DeleteProjectEvent.of(this, project));
        }

    }

    @Override
    public PageData<ProjectSummary> querySummary(final String projectName, final PageParam pageParam) {
        return this.projectRepository.querySummary(projectName, pageParam);
    }

    @Override
    public void associateUser(final Identity projectId, final Identity userId, final ProjectUserType type) {

        final Project project = this.projectRepository.find(projectId, true, false);

        CheckUtils.isNotNull(project, () -> DynamicMockAdminException.from("Project does not exist."));

        // 管理员或项目管理员可关联用户
        if (!UserHolder.isAdminUser()) {
            CheckUtils.isTrue(this.projectRepository.isProjectAdmin(projectId, UserHolder.getUserId()), () -> DynamicMockAdminException.from("Insufficient permissions."));
        }

        this.projectRepository.createProjectUser(projectId, userId, type);
    }

    @Override
    public void onApplicationEvent(final ApplicationEvent event) {

        if (event instanceof DeleteUserEvent) {

            this.projectRepository.deleteProjectUserByUserId(((DeleteUserEvent) event).getUser().getUserId());

        } else if (event instanceof DeleteMockHandlerEvent) {

            this.projectRepository.deleteProjectHandlerByHandlerId(((DeleteMockHandlerEvent) event).getHandlerId());

        } else if (event instanceof SaveMockHandlerEvent) {

            final SaveMockHandlerEvent saveMockHandlerEvent = (SaveMockHandlerEvent) event;
            final Identity projectId = saveMockHandlerEvent.getProjectId();
            if (null != projectId) {
                final Identity handlerId = saveMockHandlerEvent.getHandlerId();
                // 删除相关的handler关系重新创建
                this.projectRepository.deleteProjectHandlerByHandlerId(handlerId);
                this.projectRepository.createProjectHandler(projectId, handlerId);
            }

        }
    }
}