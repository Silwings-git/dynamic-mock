package top.silwings.admin.web.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import top.silwings.admin.DynamicMockAdminApplication;
import top.silwings.admin.common.Role;
import top.silwings.admin.web.vo.param.LoginParam;
import top.silwings.admin.web.vo.param.SaveUserParam;
import top.silwings.core.common.Identity;

import java.util.UUID;

/**
 * @ClassName LoginControllerTest
 * @Description
 * @Author Silwings
 * @Date 2022/11/20 17:39
 * @Since
 **/
@Slf4j
@Transactional
@SpringBootTest(classes = DynamicMockAdminApplication.class)
@RunWith(SpringRunner.class)
public class LoginControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private LoginController loginController;

    private SaveUserParam user = null;

    @Before
    public void before() {

        this.user = new SaveUserParam();
        this.user.setUsername(UUID.randomUUID().toString().substring(0, 16));
        this.user.setUserAccount(UUID.randomUUID().toString().substring(0, 16));
        this.user.setPassword(this.user.getUserAccount());
        this.user.setRole(Role.ADMIN_USER.getCode());

        this.userController.save(this.user);
    }

    @Test
    public void loginAdmin() {
        login();
        // TODO_Silwings: 2022/11/25 logout
    }

    private void login() {
        final LoginParam loginParam = new LoginParam();
        loginParam.setUserAccount(this.user.getUserAccount());
        loginParam.setPassword(this.user.getPassword());

        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        this.loginController.login(loginParam, mockHttpServletResponse);

        Assert.assertNotNull(mockHttpServletResponse.getCookie("dynamic-mock-login-identity"));
    }

    @Data
    public static class User {
        private Identity userId;
    }


}