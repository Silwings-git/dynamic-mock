package top.silwings.admin.repository.db.mysql;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.model.Project;
import top.silwings.admin.model.ProjectSummary;
import top.silwings.admin.repository.ProjectRepository;
import top.silwings.admin.repository.db.mysql.mapper.ProjectMapper;
import top.silwings.admin.repository.db.mysql.po.ProjectPo;
import top.silwings.core.common.Identity;

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

    public ProjectRepositoryImpl(final ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Override
    @Transactional
    public void create(final Project project, final Identity author) {

        final ProjectPo projectPo = project.toProject();

        this.projectMapper.insertSelective(projectPo);
    }

    @Override
    public void update(final Identity projectId, final Project project) {

        final Example updateCondition = new Example(ProjectPo.class);
        updateCondition.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.intValue());

        this.projectMapper.updateByConditionSelective(project.toProject(), updateCondition);
    }

    @Override
    public Project find(final Identity projectId, final boolean findUser, final boolean findHandler) {

        if (null == projectId) {
            return null;
        }

        final Example updateCondition = new Example(ProjectPo.class);
        updateCondition.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.intValue());

        final List<ProjectPo> projectPoList = this.projectMapper.selectByConditionAndRowBounds(updateCondition, new RowBounds(0, 1));
        if (CollectionUtils.isEmpty(projectPoList)) {
            return null;
        }

        return Project.from(projectPoList.get(0));
    }

    @Override
    @Transactional
    public boolean delete(final Identity projectId) {

        final Example projectCondition = new Example(ProjectPo.class);
        projectCondition.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.intValue());

        return this.projectMapper.deleteByCondition(projectCondition) > 0;
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

}