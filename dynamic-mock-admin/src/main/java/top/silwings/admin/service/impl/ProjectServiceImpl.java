package top.silwings.admin.service.impl;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.ProjectDto;
import top.silwings.admin.repository.mapper.ProjectMapper;
import top.silwings.admin.repository.po.ProjectPo;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.admin.service.ProjectService;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;

import java.util.Collections;
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
    public Identity create(final String projectName, final String baseUri) {

        final ProjectPo project = ProjectPo.builder()
                .projectName(projectName)
                .baseUri(baseUri)
                .build();

        this.projectMapper.insertSelective(project);

        return Identity.from(project.getProjectId());
    }

    @Override
    public Identity updateById(final Identity projectId, final String projectName, final String baseUri) {

        final ProjectDto original = this.find(projectId);

        final ProjectPo project = ProjectPo.builder()
                .projectName(projectName)
                .baseUri(baseUri)
                .build();

        final Example example = new Example(ProjectPo.class);
        example.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.intValue());

        this.projectMapper.updateByConditionSelective(project, example);

        // 如果basicUri发生了变化,需要重新注册任务
        if (!original.getBaseUri().equals(ConvertUtils.getNoNullOrDefault(baseUri, ""))) {
            this.mockHandlerService.reRegisterHandler(ProjectDto.from(project));
        }

        return projectId;
    }

    @Override
    public ProjectDto find(final Identity projectId) {

        final Example example = new Example(ProjectPo.class);
        example.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.intValue());

        final List<ProjectPo> projectPoList = this.projectMapper.selectByConditionAndRowBounds(example, new RowBounds(0, 1));

        CheckUtils.hasMinimumSize(projectPoList, 1, DynamicMockAdminException.supplier(ErrorCode.PROJECT_NOT_EXIST));

        return ProjectDto.from(projectPoList.get(0));
    }

    @Override
    @Transactional
    public void delete(final Identity projectId) {

        // 项目下包含Mock处理器时不允许删除
        final int handlerQuantity = this.mockHandlerService.findMockHandlerQuantityByProject(projectId);
        CheckUtils.isTrue(handlerQuantity <= 0, DynamicMockAdminException.supplier(ErrorCode.PROJECT_PROHIBIT_DELETION));

        final Example example = new Example(ProjectPo.class);
        example.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.intValue());

        this.projectMapper.deleteByCondition(example);
    }

    @Override
    public List<ProjectDto> query(final List<Identity> projectIdList, final String projectName) {

        final Example example = new Example(ProjectPo.class);
        final Example.Criteria criteria = example.createCriteria();
        criteria
                .andLike(ProjectPo.C_PROJECT_NAME, ConvertUtils.getNoBlankOrDefault(projectName, null, name -> name + "%"));

        if (null != projectIdList) {
            if (projectIdList.isEmpty()) {
                return Collections.emptyList();
            }
            criteria.andIn(ProjectPo.C_PROJECT_ID, projectIdList.stream().map(Identity::intValue).collect(Collectors.toList()));
        }

        final int total = this.projectMapper.selectCountByCondition(example);
        if (total <= 0) {
            return Collections.emptyList();
        }

        final List<ProjectPo> projectPoList = this.projectMapper.selectByCondition(example);

        return projectPoList.stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());
    }

}