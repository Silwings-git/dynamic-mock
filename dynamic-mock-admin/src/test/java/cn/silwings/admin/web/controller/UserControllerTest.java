package cn.silwings.admin.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import cn.silwings.admin.DynamicMockAdminApplication;
import cn.silwings.admin.auth.UserAuthInfo;
import cn.silwings.admin.auth.UserHolder;
import cn.silwings.admin.common.Result;
import cn.silwings.admin.common.Role;
import cn.silwings.admin.web.vo.param.ChangePasswordParam;
import cn.silwings.admin.web.vo.param.DeleteUserParam;
import cn.silwings.admin.web.vo.param.LoginParam;
import cn.silwings.admin.web.vo.param.QueryUserParam;
import cn.silwings.admin.web.vo.param.SaveUserParam;
import cn.silwings.core.common.Identity;

import java.util.UUID;

/**
 * @ClassName UserControllerTest
 * @Description
 * @Author Silwings
 * @Date 2022/11/25 22:05
 * @Since
 **/
@Slf4j
@Transactional
@SpringBootTest(classes = DynamicMockAdminApplication.class)
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private LoginController loginController;

    @Test
    public void user() {

        UserHolder.setUser(UserAuthInfo.builder().role(Role.ADMIN_USER.getCode()).build());

        final SaveUserParam userParam = this.save();
        this.changePassword(userParam);
        this.query();
        this.deleteUser(this.save().getUserId());
    }

    private SaveUserParam save() {

        final SaveUserParam saveUserParam = new SaveUserParam();
        saveUserParam.setUsername(UUID.randomUUID().toString().substring(0, 30));
        saveUserParam.setUserAccount(UUID.randomUUID().toString().substring(0, 30));
        saveUserParam.setRole(Role.ADMIN_USER.getCode());
        saveUserParam.setPassword("root");

        final Result<Identity> result = this.userController.save(saveUserParam);

        Assert.assertNotNull(result);

        saveUserParam.setUserId(result.getData());

        Assert.assertNotNull(this.userController.save(saveUserParam));

        return saveUserParam;
    }

    private void deleteUser(final Identity userId) {

        final DeleteUserParam deleteUserParam = new DeleteUserParam();
        deleteUserParam.setUserId(userId);

        Assert.assertNotNull(this.userController.deleteUser(deleteUserParam));
    }

    private void changePassword(final SaveUserParam userParam) {

        final ChangePasswordParam changePasswordParam = new ChangePasswordParam();
        changePasswordParam.setOldPassword(userParam.getPassword());
        changePasswordParam.setNewPassword(UUID.randomUUID().toString());

        final LoginParam loginParam = new LoginParam();
        loginParam.setUserAccount(userParam.getUserAccount());
        loginParam.setPassword(userParam.getPassword());


        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        this.loginController.login(loginParam, mockHttpServletResponse);

        UserHolder.setUser(UserAuthInfo.builder().userId(userParam.getUserId()).userAccount(userParam.getUserAccount()).build());


        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setCookies(mockHttpServletResponse.getCookie("dynamic-mock-login-identity"));

        Assert.assertNotNull(this.userController.changePassword(changePasswordParam, mockHttpServletRequest, new MockHttpServletResponse()));
    }

    private void query() {

        final QueryUserParam queryUserParam = new QueryUserParam();

        Assert.assertNotNull(this.userController.query(queryUserParam));
    }

}