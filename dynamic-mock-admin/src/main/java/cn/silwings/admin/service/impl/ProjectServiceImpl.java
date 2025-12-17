package cn.silwings.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import cn.silwings.admin.common.DynamicMockAdminContext;
import cn.silwings.admin.common.PageData;
import cn.silwings.admin.common.PageParam;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.admin.model.ProjectDto;
import cn.silwings.admin.repository.mapper.ProjectMapper;
import cn.silwings.admin.repository.po.ProjectPo;
import cn.silwings.admin.service.MockHandlerService;
import cn.silwings.admin.service.ProjectService;
import cn.silwings.core.common.Identity;
import cn.silwings.core.utils.CheckUtils;
import cn.silwings.core.utils.ConvertUtils;

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
@Slf4j
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

        try {
            this.projectMapper.insertSelective(project);
        } catch (DuplicateKeyException e) {
            log.error("Project更新数据失败.", e);
            throw DynamicMockAdminException.from(ErrorCode.PROJECT_DUPLICATE_BASE_URI);
        }

        return Identity.from(project.getProjectId());
    }

    @Override
    public Identity updateById(final Identity projectId, final String projectName, final String baseUri) {

        final ProjectDto original = this.find(projectId);

        final ProjectPo project = ProjectPo.builder()
                .projectId(projectId.intValue())
                .projectName(projectName)
                .baseUri(baseUri)
                .build();

        final Example example = new Example(ProjectPo.class);
        example.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.intValue());

        try {
            this.projectMapper.updateByConditionSelective(project, example);
        } catch (DuplicateKeyException e) {
            log.error("Project更新数据失败.", e);
            throw DynamicMockAdminException.from(ErrorCode.PROJECT_DUPLICATE_BASE_URI);
        }

        // 如果basicUri发生了变化,需要重新注册任务
        if (!original.getBaseUri().equals(ConvertUtils.getNoNullOrDefault(baseUri, ""))) {
            DynamicMockAdminContext.getInstance()
                    .getMockHandlerRegisterService()
                    .refreshRegisteredHandlerByProject(Identity.from(project.getProjectId()));
        }

        return projectId;
    }

    @Override
    public ProjectDto find(final Identity projectId) {

        final Example example = new Example(ProjectPo.class);
        example.createCriteria()
                .andEqualTo(ProjectPo.C_PROJECT_ID, projectId.intValue());

        final List<ProjectPo> projectPoList = this.projectMapper.selectByConditionAndRowBounds(example, PageParam.oneRow());

        CheckUtils.isNotEmpty(projectPoList, DynamicMockAdminException.supplier(ErrorCode.PROJECT_NOT_EXIST));

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
    public List<ProjectDto> queryOwnAll(final List<Identity> projectIdList) {

        if (CollectionUtils.isEmpty(projectIdList)) {
            return Collections.emptyList();
        }

        final Example example = new Example(ProjectPo.class);
        example.createCriteria()
                .andIn(ProjectPo.C_PROJECT_ID, Identity.toInt(projectIdList));

        example.orderBy(ProjectPo.C_PROJECT_NAME).asc();

        final List<ProjectPo> projectPoList = this.projectMapper.selectByCondition(example);

        return projectPoList.stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 分页查询项目信息
     *
     * @param projectIdList 项目id集,如果为null表示查询所有
     * @param projectName   项目名称
     * @param pageParam     分页参数
     * @return 项目信息集
     */
    @Override
    public PageData<ProjectDto> query(final List<Identity> projectIdList, final String projectName, final PageParam pageParam) {

        if (CollectionUtils.isEmpty(projectIdList)) {
            return PageData.empty();
        }

        final Example example = new Example(ProjectPo.class);
        example.createCriteria()
                .andLike(ProjectPo.C_PROJECT_NAME, ConvertUtils.getNoBlankOrDefault(projectName, null, name -> name + "%"))
                .andIn(ProjectPo.C_PROJECT_ID, Identity.toInt(projectIdList));

        example.orderBy(ProjectPo.C_PROJECT_NAME).asc();

        final int total = this.projectMapper.selectCountByCondition(example);
        if (total <= 0) {
            return PageData.empty();
        }

        final List<ProjectPo> projectPoList = this.projectMapper.selectByConditionAndRowBounds(example, pageParam.toRowBounds());

        final List<ProjectDto> projectList = projectPoList.stream()
                .map(ProjectDto::from)
                .collect(Collectors.toList());

        return PageData.of(projectList, total);
    }

    @Override
    public List<Identity> queryAllProjectId() {

        final Example example = new Example(ProjectPo.class);
        example.selectProperties(ProjectPo.C_PROJECT_ID);

        return this.projectMapper.selectByCondition(example)
                .stream()
                .map(ProjectPo::getProjectId)
                .map(Identity::from)
                .collect(Collectors.toList());
    }

}