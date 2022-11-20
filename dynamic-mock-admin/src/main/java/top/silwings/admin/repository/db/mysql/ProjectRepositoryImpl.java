package top.silwings.admin.repository.db.mysql;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.common.enums.ProjectUserType;
import top.silwings.admin.model.Project;
import top.silwings.admin.model.ProjectSummary;
import top.silwings.admin.repository.ProjectRepository;
import top.silwings.admin.repository.db.mysql.mapper.ProjectMapper;
import top.silwings.admin.repository.db.mysql.mapper.ProjectMockHandlerMapper;
import top.silwings.admin.repository.db.mysql.mapper.ProjectUserMapper;
import top.silwings.admin.repository.db.mysql.po.ProjectMockHandlerPo;
import top.silwings.admin.repository.db.mysql.po.ProjectPo;
import top.silwings.admin.repository.db.mysql.po.ProjectUserPo;
import top.silwings.core.common.Identity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ProjectRepositoryImpl
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 19:40
 * @Since
 **/
@Service
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectMapper projectMapper;
    private final ProjectUserMapper projectUserMapper;

    private final ProjectMockHandlerMapper projectMockHandlerMapper;

    public ProjectRepositoryImpl(final ProjectMapper projectMapper, final ProjectUserMapper projectUserMapper, final ProjectMockHandlerMapper projectMockHandlerMapper) {
        this.projectMapper = projectMapper;
        this.projectUserMapper = projectUserMapper;
        this.projectMockHandlerMapper = projectMockHandlerMapper;
    }

    @Override
    @Transactional
    public void create(final Project project, final Identity author) {

        final ProjectPo projectPo = project.toProject();

        this.projectMapper.insertSelective(projectPo);

        final ProjectUserPo projectUserPo = new ProjectUserPo();
        projectUserPo.setProjectId(projectPo.getProjectId());
        projectUserPo.setUserId(author.longValue());
        projectUserPo.setType(ProjectUserType.MANAGER.code());
        this.projectUserMapper.insertSelective(projectUserPo);
    }

    @Override
    public void update(final Identity projectId, final Project project) {

        final Example updateCondition = new Example(ProjectPo.class);
        updateCondition.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.longValue());

        this.projectMapper.updateByConditionSelective(project.toProject(), updateCondition);
    }

    @Override
    public Project find(final Identity projectId, final boolean findUser, final boolean findHandler) {

        if (null == projectId) {
            return null;
        }

        final Example updateCondition = new Example(ProjectPo.class);
        updateCondition.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.longValue());

        final List<ProjectPo> projectPoList = this.projectMapper.selectByConditionAndRowBounds(updateCondition, new RowBounds(0, 1));
        if (CollectionUtils.isEmpty(projectPoList)) {
            return null;
        }


        List<ProjectMockHandlerPo> projectMockHandlerPoList = Collections.emptyList();

        if (findHandler) {
            final Example handlerCondition = new Example(ProjectUserPo.class);
            handlerCondition.createCriteria()
                    .andEqualTo(ProjectUserPo.C_PROJECT_ID, projectId.longValue());

            projectMockHandlerPoList = this.projectMockHandlerMapper.selectByCondition(handlerCondition);
        }

        return Project.from(projectPoList.get(0),  projectMockHandlerPoList);
    }

    @Override
    public boolean isProjectAdmin(final Identity projectId, final Identity userId) {
        final Example userCondition = new Example(ProjectUserPo.class);
        userCondition.createCriteria()
                .andEqualTo(ProjectUserPo.C_PROJECT_ID, projectId.longValue())
                .andEqualTo(ProjectUserPo.C_USER_ID, userId.longValue());

        return CollectionUtils.isNotEmpty(this.projectUserMapper.selectByConditionAndRowBounds(userCondition, new RowBounds(0, 1)));
    }


    @Override
    @Transactional
    public boolean delete(final Identity projectId) {

        final Example projectCondition = new Example(ProjectPo.class);
        projectCondition.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.longValue());

        final int row = this.projectMapper.deleteByCondition(projectCondition);

        // 删除项目用户关系
        final Example userCondition = new Example(ProjectUserPo.class);
        userCondition.createCriteria()
                .andEqualTo(ProjectUserPo.C_PROJECT_ID, projectId.longValue());

        this.projectUserMapper.deleteByCondition(userCondition);

        // 删除项目-Mock处理器关系
        final Example handlerCondition = new Example(ProjectMockHandlerPo.class);
        handlerCondition.createCriteria()
                .andEqualTo(ProjectMockHandlerPo.C_PROJECT_ID, projectId.longValue());

        this.projectMockHandlerMapper.deleteByCondition(handlerCondition);

        return row > 0;
    }

    @Override
    public PageData<ProjectSummary> querySummary(final String projectName, final PageParam pageParam) {

        final Example queryCondition = new Example(ProjectPo.class);

        if (StringUtils.isNotBlank(projectName)) {
            queryCondition.createCriteria()
                    .andLike(ProjectPo.C_PROJECT_NAME, projectName);
        }

        final int total = projectMapper.selectCountByCondition(queryCondition);
        if (total <= 0) {
            return PageData.empty();
        }

        final List<ProjectPo> projectPoList = this.projectMapper.selectByConditionAndRowBounds(queryCondition, pageParam.toRowBounds());

        final List<ProjectSummary> projectSummaryList = projectPoList.stream()
                .map(ProjectSummary::from)
                .collect(Collectors.toList());

        return PageData.of(projectSummaryList, total);
    }

    @Override
    public void createProjectUser(final Identity projectId, final Identity userId, final ProjectUserType type) {

        final ProjectUserPo projectUserPo = new ProjectUserPo();
        projectUserPo.setProjectId(projectId.longValue());
        projectUserPo.setUserId(userId.longValue());
        projectUserPo.setType(type.code());

        this.projectUserMapper.insertSelective(projectUserPo);
    }

    @Override
    public void deleteProjectUserByUserId(final Identity userId) {
        final Example delCondition = new Example(ProjectUserPo.class);
        delCondition.createCriteria()
                .andEqualTo(ProjectUserPo.C_USER_ID, userId.longValue());

        this.projectUserMapper.deleteByCondition(delCondition);
    }

    @Override
    public void deleteProjectHandlerByHandlerId(final Identity handlerId) {
        final Example delCondition = new Example(ProjectMockHandlerPo.class);
        delCondition.createCriteria()
                .andEqualTo(ProjectMockHandlerPo.C_HANDLER_ID, handlerId.longValue());

        this.projectMockHandlerMapper.deleteByCondition(delCondition);
    }

    @Override
    public void createProjectHandler(final Identity projectId, final Identity handlerId) {
        final ProjectMockHandlerPo projectMockHandlerPo = new ProjectMockHandlerPo();
        projectMockHandlerPo.setProjectId(projectId.longValue());
        projectMockHandlerPo.setHandlerId(handlerId.longValue());
        this.projectMockHandlerMapper.insertSelective(projectMockHandlerPo);
    }
}