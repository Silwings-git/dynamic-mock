package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import top.silwings.admin.model.User;
import top.silwings.admin.service.LoginService;
import top.silwings.admin.service.UserService;
import top.silwings.admin.web.vo.param.ChangePasswordParam;
import top.silwings.admin.web.vo.param.SaveUserParam;
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
@RequestMapping("/dynamic/mock/user")
@Api(value = "用户管理")
public class UserController {

    private final UserService userService;

    private final LoginService loginService;

    public UserController(final UserService userService, final LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @PostMapping("/save")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "保存用户")
    public Result<Void> save(@RequestBody final SaveUserParam saveUserParam) {

        saveUserParam.validate();

        if (null != saveUserParam.getUserId()) {

            final Identity userId = Identity.from(saveUserParam.getUserId());
            this.userService.update(userId, saveUserParam.getUsername(), saveUserParam.getPassword(), saveUserParam.getRole());
        } else {

            this.userService.create(saveUserParam.getUsername(), saveUserParam.getUserAccount(), saveUserParam.getUserAccount(), saveUserParam.getRole());
        }

        return Result.ok();
    }

    @DeleteMapping("/del/{userId}")
    @PermissionLimit(adminUser = true)
    @ApiOperation(value = "删除用户")
    public Result<Void> deleteUser(@PathVariable("userId") final String userId) {

        this.userService.deleteUser(Identity.from(userId));

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

    @GetMapping("/query/{pageNum}/{pageSize}")
    @PermissionLimit
    @ApiOperation(value = "分页查询用户列表")
    public PageResult<UserResult> query(@PathVariable("pageNum") final Integer pageNum,
                                        @PathVariable("pageSize") final Integer pageSize,
                                        @RequestParam("searchKey") final String searchKey) {

        // TODO_Silwings: 2022/11/20 补充项目条件

        final PageData<User> pageData = this.userService.query(searchKey, PageParam.of(pageNum, pageSize));

        final List<UserResult> userResultList = pageData.getList().stream()
                .map(UserResult::from)
                .collect(Collectors.toList());

        return PageResult.ok(userResultList, pageData.getTotal());
    }

}