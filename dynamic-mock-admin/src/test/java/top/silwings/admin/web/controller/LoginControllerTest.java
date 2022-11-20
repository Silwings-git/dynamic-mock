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
import top.silwings.admin.common.enums.Role;
import top.silwings.admin.web.vo.param.SaveUserParam;
import top.silwings.admin.web.vo.param.UserLoginParam;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.JsonUtils;

import java.util.Random;

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
        final Random random = new Random();

        this.user = new SaveUserParam();
        this.user.setUsername(String.valueOf(random.nextInt(100)));
        this.user.setUserAccount(String.valueOf(random.nextInt(100)));
        this.user.setPassword(this.user.getUserAccount());
        this.user.setRole(Role.ADMIN_USER.getCode());

        this.userController.save(this.user);
    }

    @Test
    public void loginAdmin() {
        login();
    }

    @Test
    public void testIdentity() {
        final User user = new User();
        user.setUserId(Identity.from("20"));

        log.info("jackson toJson: {}", JsonUtils.toJSONString(user));
        log.info("jackson toBean: {}", JsonUtils.toBean(JsonUtils.toJSONString(user), User.class));

        final Object o = JsonUtils.toBean(JsonUtils.toJSONString(MockHandlerDefinitionMock.build()), Object.class);
        System.out.println("o = " + o);
    }

    @Data
    public static class User {
        private Identity userId;
    }


    private void login() {
        final UserLoginParam loginParam = new UserLoginParam();
        loginParam.setUserAccount(this.user.getUserAccount());
        loginParam.setPassword(this.user.getPassword());

        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        this.loginController.login(loginParam, mockHttpServletResponse);

        Assert.assertNotNull(mockHttpServletResponse.getCookie("dynamic-mock-login-identity"));
    }


}