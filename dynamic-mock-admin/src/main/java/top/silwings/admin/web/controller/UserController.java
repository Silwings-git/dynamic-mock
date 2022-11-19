package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageResult;
import top.silwings.admin.common.Result;
import top.silwings.admin.model.User;
import top.silwings.admin.service.UserService;
import top.silwings.admin.web.vo.param.ChangePasswordParam;
import top.silwings.admin.web.vo.param.QueryUserParam;
import top.silwings.admin.web.vo.param.ResetPasswordParam;
import top.silwings.admin.web.vo.param.UserParam;
import top.silwings.admin.web.vo.result.UserResult;

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
@RequestMapping("/dynamic/mock/user")
@Api(value = "用户管理")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "创建用户")
    public Result<Void> save(@RequestBody final UserParam userParam) {

        this.userService.createUser(userParam.getUsername(), userParam.getUserAccount(), userParam.getRole());

        return Result.ok();
    }

    @DeleteMapping("/del/{userAccount}")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "删除用户")
    public Result<Void> deleteUser(@PathVariable("userAccount") final String userAccount) {

        this.userService.deleteUser(userAccount);

        return Result.ok();
    }

    @PostMapping("/resetPassword")
    @PermissionLimit(adminUser = true)
    public Result<Void> resetPassword(@RequestBody final ResetPasswordParam param) {

        this.userService.resetPassword(param);

        return Result.ok();
    }

    @PostMapping("/changePassword")
    @PermissionLimit
    @ApiOperation(value = "修改密码")
    public Result<Void> changePassword(@RequestBody final ChangePasswordParam param) {

        this.userService.changePassword(param.getOldPassword(), param.getNewPassword());

        return Result.ok();
    }

    @PostMapping("/query")
    @PermissionLimit
    @ApiOperation(value = "分页查询用户列表")
    public PageResult<UserResult> query(@RequestBody final QueryUserParam param) {

        final PageData<User> pageData = this.userService.query(param.getSearchKey(), param);

        final List<UserResult> userResultList = pageData.getList().stream()
                .map(UserResult::from)
                .collect(Collectors.toList());

        return PageResult.ok(userResultList, pageData.getTotal());
    }

}