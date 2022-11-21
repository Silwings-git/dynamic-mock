package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.common.Result;
import top.silwings.admin.service.LoginService;
import top.silwings.admin.web.vo.param.LoginParam;

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
    @ApiOperation(value = "登录")
    public Result<String> login(@RequestBody LoginParam loginParam, final HttpServletResponse response) {

        loginParam.validate();

        final String username = this.loginService.login(loginParam.getUserAccount(), loginParam.getPassword(), Boolean.TRUE.equals(loginParam.getIfRemember()), response);

        return Result.ok(username);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "登出")
    public Result<Void> logout(final HttpServletRequest request, final HttpServletResponse response) {

        this.loginService.logout(request, response);

        return Result.ok();
    }

}