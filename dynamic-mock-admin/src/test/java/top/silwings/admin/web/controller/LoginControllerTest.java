package top.silwings.admin.web.controller;

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
import top.silwings.admin.model.User;
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

    private void login() {
        final UserLoginParam loginParam = new UserLoginParam();
        loginParam.setUserAccount(this.user.getUserAccount());
        loginParam.setPassword(this.user.getPassword());

        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        this.loginController.login(loginParam, new MockHttpServletResponse());

        Assert.assertNotNull(mockHttpServletResponse.getCookie("dynamic-mock-login-identity"));
    }

    public static void main(String[] args) {
        final String ddd = JsonUtils.toJSONString(User.builder().userId(Identity.from(1L)).userAccount("ddd").build());
        System.out.println(ddd);

        final User user1 = JsonUtils.toBean(ddd, User.class);
        System.out.println("user1 = " + user1);
    }

}