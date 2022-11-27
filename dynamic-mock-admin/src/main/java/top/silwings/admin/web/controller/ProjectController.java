package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageResult;
import top.silwings.admin.common.Result;
import top.silwings.admin.model.ProjectDto;
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
@RequestMapping("/dynamic-mock/project")
@Api(value = "项目管理")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(final ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/save")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "保存项目信息")
    public Result<Identity> save(@RequestBody SaveProjectParam param) {

        param.validate();

        final Identity projectId;

        if (null == param.getProjectId()) {
            projectId = this.projectService.create(param.getProjectName(), param.getBaseUri());
        } else {
            projectId = this.projectService.updateById(param.getProjectId(), param.getProjectName(), param.getBaseUri());
        }

        return Result.ok(projectId);
    }

    @PostMapping("/query")
    @PermissionLimit
    @ApiOperation(value = "分页查询项目信息")
    public PageResult<ProjectResult> query(@RequestBody final QueryProjectParam param) {

        final List<Identity> projectIdList = UserHolder.isAdminUser() ? null : UserHolder.getUser().getPermissionList();

        final PageData<ProjectDto> projectPageData = this.projectService.query(projectIdList, param.getProjectName(), param);

        final List<ProjectResult> resultList = projectPageData
                .getList()
                .stream()
                .map(ProjectResult::from)
                .collect(Collectors.toList());

        return PageResult.ok(resultList, projectPageData.getTotal());
    }

    @PostMapping("/queryAll")
    @PermissionLimit
    @ApiOperation(value = "查询全部项目信息")
    public Result<List<ProjectResult>> queryAll() {

        final List<Identity> projectIdList = UserHolder.isAdminUser() ? null : UserHolder.getUser().getPermissionList();

        final List<ProjectResult> resultList = this.projectService.queryAll(projectIdList)
                .stream()
                .map(ProjectResult::from)
                .collect(Collectors.toList());

        return Result.ok(resultList);
    }


    @PostMapping("/del")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "删除项目信息")
    public Result<Void> delete(@RequestBody final DeleteProjectParam param) {

        param.validate();

        this.projectService.delete(param.getProjectId());

        return Result.ok();
    }

}