package top.silwings.admin.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.service.LoginService;
import top.silwings.admin.web.vo.param.UserLoginParam;
import top.silwings.core.common.Result;

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
@RequestMapping("/dynamic/mock/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginParam userLoginParam, final HttpServletResponse response) {

        final String username = this.loginService.login(userLoginParam.getUserAccount(), userLoginParam.getPassword(), Boolean.TRUE.equals(userLoginParam.getIfRemember()), response);

        return Result.ok(username);
    }

    @PostMapping("/logout")
    public Result<Void> logout(final HttpServletRequest request, final HttpServletResponse response) {

        this.loginService.logout(request, response);

        return Result.ok();
    }

}