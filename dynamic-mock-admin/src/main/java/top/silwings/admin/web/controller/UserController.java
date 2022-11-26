package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageResult;
import top.silwings.admin.common.Result;
import top.silwings.admin.model.ProjectDto;
import top.silwings.admin.model.UserDto;
import top.silwings.admin.service.LoginService;
import top.silwings.admin.service.ProjectService;
import top.silwings.admin.service.UserService;
import top.silwings.admin.web.vo.param.ChangePasswordParam;
import top.silwings.admin.web.vo.param.DeleteUserParam;
import top.silwings.admin.web.vo.param.QueryUserParam;
import top.silwings.admin.web.vo.param.SaveUserParam;
import top.silwings.admin.web.vo.result.UserPermissionResult;
import top.silwings.admin.web.vo.result.UserResult;
import top.silwings.core.common.Identity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName UserController
 * @Description 用户管理
 * @Author Silwings
 * @Date 2022/11/19 17:45
 * @Since
 **/
@RestController
@RequestMapping("/dynamic-mock/user")
@Api(value = "用户管理")
public class UserController {

    private final UserService userService;

    private final LoginService loginService;

    private final ProjectService projectService;

    public UserController(final UserService userService, final LoginService loginService, final ProjectService projectService) {
        this.userService = userService;
        this.loginService = loginService;
        this.projectService = projectService;
    }

    @PostMapping("/save")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "保存用户")
    public Result<Identity> save(@RequestBody final SaveUserParam saveUserParam) {

        saveUserParam.validate();

        final Identity userId;

        if (null == saveUserParam.getUserId()) {

            userId = this.userService.create(saveUserParam.getUsername(), saveUserParam.getUserAccount(), saveUserParam.getUserAccount(), saveUserParam.getRole(), saveUserParam.getPermissionList());

        } else {

            userId = this.userService.updateById(saveUserParam.getUserId(), saveUserParam.getUsername(), saveUserParam.getPassword(), saveUserParam.getRole(), saveUserParam.getPermissionList());
        }

        return Result.ok(userId);
    }

    @PostMapping("/del")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "删除用户")
    public Result<Void> deleteUser(@RequestBody final DeleteUserParam param) {

        param.validate();

        this.userService.deleteUser(param.getUserId());

        return Result.ok();
    }

    @PostMapping("/changePassword")
    @PermissionLimit
    @ApiOperation(value = "修改密码")
    public Result<Void> changePassword(@RequestBody final ChangePasswordParam param, final HttpServletRequest request, final HttpServletResponse response) {

        param.validate();

        this.userService.changePassword(param.getOldPassword(), param.getNewPassword());

        this.loginService.logout(request, response);

        return Result.ok();
    }

    @PostMapping("/query")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "分页查询用户列表")
    public PageResult<UserResult> query(@RequestBody final QueryUserParam param) {

        final PageData<UserDto> pageData = this.userService.query(param.getUsername(), param.getUserAccount(), param.getRole(), param);

        final List<ProjectDto> projectList = this.projectService.queryAll();

        final List<UserResult> userResultList = pageData.getList().stream()
                .map(user -> UserResult.of(user,
                        projectList.stream()
                                .map(project ->
                                        UserPermissionResult.builder()
                                                .projectName(project.getProjectName())
                                                .projectId(project.getProjectId())
                                                .own(user.getPermissionList().contains(project.getProjectId()))
                                                .build())
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return PageResult.ok(userResultList, pageData.getTotal());
    }

}