package top.silwings.admin.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.UserDto;
import top.silwings.admin.service.LoginService;
import top.silwings.admin.service.UserService;
import top.silwings.admin.utils.CookieUtils;
import top.silwings.admin.utils.EncryptUtils;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * @ClassName LoginServiceImpl
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:20
 * @Since
 **/
@Service
public class LoginServiceImpl implements LoginService {

    private static final String LOGIN_IDENTITY_KEY = "dynamic-mock-login-identity";

    private final UserService userService;

    public LoginServiceImpl(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDto login(final String userAccount, final String password, final boolean ifRemember, final HttpServletResponse response) {

        CheckUtils.isNotBlank(userAccount, DynamicMockAdminException.supplier(ErrorCode.LOGIN_ACCOUNT_PASSWORD_INCORRECT));
        CheckUtils.isNotBlank(password, DynamicMockAdminException.supplier(ErrorCode.LOGIN_ACCOUNT_PASSWORD_INCORRECT));

        final UserDto user = this.userService.findByUserAccount(userAccount, false);
        CheckUtils.isNotNull(user, DynamicMockAdminException.supplier(ErrorCode.LOGIN_ACCOUNT_PASSWORD_INCORRECT));

        final String loginPassword = EncryptUtils.encryptPassword(password);
        CheckUtils.isEquals(user.getPassword(), loginPassword, DynamicMockAdminException.supplier(ErrorCode.LOGIN_ACCOUNT_PASSWORD_INCORRECT));

        final String userAuthToken = this.makeToken(user);

        CookieUtils.set(response, LOGIN_IDENTITY_KEY, userAuthToken, ifRemember);

        return user;
    }

    private String makeToken(final UserDto user) {
        final String tokenJson = JsonUtils.toJSONString(user);
        return new BigInteger(tokenJson.getBytes()).toString(16);
    }

    @Override
    public UserDto ifLogin(final HttpServletRequest request, final HttpServletResponse response) {

        // TODO_Silwings: 2022/11/28
        final String token = true ? "7b22757365724964223a312c22757365726e616d65223a224d6973616b61204d696b6f746f222c22757365724163636f756e74223a22726f6f74222c2270617373776f7264223a223633613966306561376262393830353037393662363439653835343831383435222c22726f6c65223a312c227065726d697373696f6e4c697374223a5b5d7d"
                : CookieUtils.getValue(request, LOGIN_IDENTITY_KEY);

        try {
            final UserDto cookieUser = this.parseToken(token);
            if (cookieUser != null) {

                final UserDto dbUser = this.userService.findByUserAccount(cookieUser.getUserAccount(), false);

                if (null == dbUser) {
                    this.logout(request, response);
                    return null;
                }

                if (cookieUser.getRole() != dbUser.getRole()) {
                    // 角色变更,需要重新登录
                    this.logout(request, response);
                    return null;
                }

                if (cookieUser.getPassword().equals(dbUser.getPassword())) {
                    return dbUser;
                }
            }
        } catch (Exception e) {
            this.logout(request, response);
        }

        return null;
    }

    private UserDto parseToken(final String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }

        final String tokenJson = new String(new BigInteger(token, 16).toByteArray());

        return UserDto.from(tokenJson);
    }

    @Override
    public void logout(final HttpServletRequest request, final HttpServletResponse response) {
        CookieUtils.remove(request, response, LOGIN_IDENTITY_KEY);
    }

}