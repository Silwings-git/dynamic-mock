package cn.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.silwings.admin.auth.annotation.PermissionLimit;
import cn.silwings.admin.common.Result;
import cn.silwings.admin.model.UserDto;
import cn.silwings.admin.service.LoginService;
import cn.silwings.admin.web.vo.param.LoginParam;
import cn.silwings.admin.web.vo.result.LoginResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName AuthController
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:21
 * @Since
 **/
@RestController
@RequestMapping("/dynamic-mock/auth")
@Api(value = "登录控制")
public class LoginController {

    private final LoginService loginService;

    public LoginController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    @PermissionLimit(limit = false)
    @ApiOperation(value = "登录.登录成功后返回用户名")
    public Result<LoginResult> login(@RequestBody LoginParam loginParam, final HttpServletResponse response) {

        loginParam.validate();

        final UserDto user = this.loginService.login(loginParam.getUserAccount(), loginParam.getPassword(), Boolean.TRUE.equals(loginParam.getRemember()), response);

        return Result.ok(LoginResult.from(user));
    }

    @PostMapping("/logout")
    @PermissionLimit(limit = false)
    @ApiOperation(value = "登出")
    public Result<Void> logout(final HttpServletRequest request, final HttpServletResponse response) {

        this.loginService.logout(request, response);

        return Result.ok();
    }

}