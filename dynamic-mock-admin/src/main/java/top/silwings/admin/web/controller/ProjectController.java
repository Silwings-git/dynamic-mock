package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.common.PageResult;
import top.silwings.admin.common.Result;
import top.silwings.admin.common.enums.ProjectUserType;
import top.silwings.admin.model.ProjectSummary;
import top.silwings.admin.service.ProjectService;
import top.silwings.admin.web.vo.param.AssociateUserParam;
import top.silwings.admin.web.vo.param.SaveProjectParam;
import top.silwings.admin.web.vo.result.ProjectResult;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.ConvertUtils;

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
    @PermissionLimit
    @ApiOperation(value = "保存项目信息")
    public Result<Void> save(@RequestBody SaveProjectParam param) {

        param.validate();

        this.projectService.save(ConvertUtils.getNoBlankOrDefault(param.getProjectId(), null, Identity::from),
                param.getProjectName(),
                param.getBaseUri());

        return Result.ok();
    }

    @PostMapping("/del/{projectId}")
    @PermissionLimit
    @ApiOperation(value = "删除项目信息")
    public Result<Void> delete(@PathVariable("projectId") final String projectId) {

        this.projectService.delete(Identity.from(projectId));

        return Result.ok();
    }

    @GetMapping("/query/{pageNum}/{pageSize}")
    @PermissionLimit
    @ApiOperation(value = "删除项目信息")
    public PageResult<ProjectResult> query(@PathVariable("pageNum") final Integer pageNum,
                                           @PathVariable("pageSize") final Integer pageSize,
                                           @RequestParam("projectName") final String projectName) {

        final PageData<ProjectSummary> projectPageData = this.projectService.querySummary(projectName, PageParam.of(pageNum, pageSize));

        final List<ProjectResult> projectResultList = projectPageData.getList().stream()
                .map(ProjectResult::from)
                .collect(Collectors.toList());

        return PageResult.ok(projectResultList, projectPageData.getTotal());
    }

    @PostMapping("/associate")
    @PermissionLimit
    @ApiOperation(value = "删除项目信息")
    public Result<Void> associateUser(@RequestBody final AssociateUserParam param) {

        param.validate();

        final ProjectUserType type = ProjectUserType.valueOfCode(param.getType());

        this.projectService.associateUser(Identity.from(param.getProjectId()), Identity.from(param.getUserId()), type);

        return Result.ok();
    }

}