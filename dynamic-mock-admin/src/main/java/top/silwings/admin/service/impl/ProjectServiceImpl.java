package top.silwings.admin.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.model.Project;
import top.silwings.admin.repository.db.mysql.mapper.ProjectMapper;
import top.silwings.admin.repository.db.mysql.po.ProjectPo;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.admin.service.ProjectService;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ProjectServiceImpl
 * @Description 项目管理
 * @Author Silwings
 * @Date 2022/11/20 13:54
 * @Since
 **/
@Service
public class ProjectServiceImpl implements ProjectService {


    private final ProjectMapper projectMapper;

    private final MockHandlerService mockHandlerService;

    public ProjectServiceImpl(final ProjectMapper projectMapper, final MockHandlerService mockHandlerService) {
        this.projectMapper = projectMapper;
        this.mockHandlerService = mockHandlerService;
    }

    @Override
    public void create(final String projectName, final String baseUri) {

        final ProjectPo project = ProjectPo.builder()
                .projectName(projectName)
                .baseUri(baseUri)
                .build();

        this.projectMapper.insertSelective(project);
    }

    @Override
    public void updateById(final Identity projectId, final String projectName, final String baseUri) {

        final ProjectPo project = ProjectPo.builder()
                .projectName(projectName)
                .baseUri(baseUri)
                .build();

        final Example example = new Example(ProjectPo.class);
        example.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.intValue());

        this.projectMapper.updateByConditionSelective(project, example);
    }


    @Override
    @Transactional
    public void delete(final Identity projectId) {

        // 项目下包含Mock处理器时不允许删除
        final int handlerQuantity = this.mockHandlerService.findMockHandlerQuantityByProject(projectId);
        CheckUtils.isTrue(handlerQuantity <= 0, () -> DynamicMockAdminException.from("Delete not allowed."));

        final Example example = new Example(ProjectPo.class);
        example.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.intValue());

        this.projectMapper.deleteByCondition(example);
    }

    @Override
    public PageData<Project> query(final String projectName, final PageParam pageParam) {

        final Example example = new Example(ProjectPo.class);
        example.createCriteria()
                .andLike(ProjectPo.C_PROJECT_NAME, ConvertUtils.getNoBlankOrDefault(projectName, null, name -> name + "%"));

        final int total = this.projectMapper.selectCountByCondition(example);
        if (total <= 0) {
            return PageData.empty();
        }

        final List<ProjectPo> projectPoList = this.projectMapper.selectByConditionAndRowBounds(example, pageParam.toRowBounds());

        final List<Project> projectList = projectPoList.stream()
                .map(Project::from)
                .collect(Collectors.toList());

        return PageData.of(projectList, total);
    }


}