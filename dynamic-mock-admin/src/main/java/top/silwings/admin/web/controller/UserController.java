package top.silwings.admin.web.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.service.UserService;
import top.silwings.admin.web.vo.param.ChangePasswordParam;
import top.silwings.admin.web.vo.param.UserParam;
import top.silwings.core.common.Result;

/**
 * @ClassName UserController
 * @Description 用户管理
 * @Author Silwings
 * @Date 2022/11/19 17:45
 * @Since
 **/
@RestController
@RequestMapping("/dynamic/mock/user")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "创建用户")
    public Result<Void> createUser(@RequestBody final UserParam userParam) {

        this.userService.createUser(userParam.getUsername(), userParam.getUserAccount(), userParam.getRole());

        return Result.ok();
    }

    @PostMapping("/changePassword")
    @PermissionLimit
    @ApiOperation(value = "修改密码")
    public Result<Void> changePassword(@RequestBody final ChangePasswordParam param) {

        this.userService.changePassword(param.getOldPassword(), param.getNewPassword());

        return Result.ok();
    }

    @DeleteMapping("/{userAccount}")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "删除用户")
    public Result<Void> deleteUser(@PathVariable("userAccount") final String userAccount) {

        this.userService.deleteUser(userAccount);

        return Result.ok();
    }

}