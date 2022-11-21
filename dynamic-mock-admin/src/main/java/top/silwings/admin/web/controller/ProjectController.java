package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageResult;
import top.silwings.admin.common.Result;
import top.silwings.admin.model.Project;
import top.silwings.admin.service.ProjectService;
import top.silwings.admin.web.vo.param.DeleteProjectParam;
import top.silwings.admin.web.vo.param.QueryProjectParam;
import top.silwings.admin.web.vo.param.SaveProjectParam;
import top.silwings.admin.web.vo.result.ProjectResult;
import top.silwings.core.common.Identity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ProjectController
 * @Description 项目管理
 * @Author Silwings
 * @Date 2022/11/19 19:40
 * @Since
 **/
@RestController
@RequestMapping("/dynamic/mock/project")
@Api(value = "项目管理")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(final ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/save")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "保存项目信息")
    public Result<Void> save(@RequestBody SaveProjectParam param) {

        param.validate();

        if (StringUtils.isBlank(param.getProjectId())) {
            this.projectService.create(param.getProjectName(), param.getBaseUri());
        } else {
            this.projectService.updateById(Identity.from(param.getProjectId()), param.getProjectName(), param.getBaseUri());
        }

        return Result.ok();
    }

    @PostMapping("/query")
    @PermissionLimit
    @ApiOperation(value = "分页查询项目信息")
    public PageResult<ProjectResult> query(@RequestBody final QueryProjectParam param) {

        final List<Identity> projectIdList = UserHolder.isAdminUser() ? null : UserHolder.getUser().getPermission();

        final PageData<Project> projectPageData = this.projectService.query(projectIdList, param.getProjectName(), param);
        final List<ProjectResult> projectResultList = projectPageData.getList().stream()
                .map(ProjectResult::from)
                .collect(Collectors.toList());

        return PageResult.ok(projectResultList, projectPageData.getTotal());
    }

    @PostMapping("/del")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "删除项目信息")
    public Result<Void> delete(@RequestBody final DeleteProjectParam param) {

        param.validate();

        this.projectService.delete(Identity.from(param.getProjectId()));

        return Result.ok();
    }

}