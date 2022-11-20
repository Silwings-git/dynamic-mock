package top.silwings.admin.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.model.User;
import top.silwings.admin.repository.UserRepository;
import top.silwings.admin.service.LoginService;
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

    private final UserRepository userRepository;

    public LoginServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String login(final String userAccount, final String password, final boolean ifRemember, final HttpServletResponse response) {

        CheckUtils.isNotBlank(userAccount, () -> DynamicMockAdminException.from("Username or password is empty."));
        CheckUtils.isNotBlank(password, () -> DynamicMockAdminException.from("Username or password is empty."));

        final User user = this.userRepository.findByUserAccount(userAccount);
        CheckUtils.isNotNull(user, () -> DynamicMockAdminException.from("Username or password error."));

        final String loginPassword = EncryptUtils.encryptPassword(password);
        CheckUtils.isEquals(user.getPassword(), loginPassword, () -> DynamicMockAdminException.from("Username or password error."));

        final String userAuthToken = this.makeToken(user);

        CookieUtils.set(response, LOGIN_IDENTITY_KEY, userAuthToken, ifRemember);

        return user.getUsername();
    }

    private String makeToken(final User user) {
        final String tokenJson = JsonUtils.toJSONString(user);
        return new BigInteger(tokenJson.getBytes()).toString(16);
    }

    @Override
    public User ifLogin(final HttpServletRequest request, final HttpServletResponse response) {

        final String token = CookieUtils.getValue(request, LOGIN_IDENTITY_KEY);

        try {
            final User cookieUser = this.parseToken(token);
            if (cookieUser != null) {

                final User dbUser = this.userRepository.findByUserAccount(cookieUser.getUserAccount());

                if (null == dbUser) {
                    this.logout(request, response);
                }

                if (dbUser != null && cookieUser.getPassword().equals(dbUser.getPassword())) {
                    return dbUser;
                }
            }
        } catch (Exception e) {
            this.logout(request, response);
        }

        return null;
    }

    private User parseToken(final String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }

        final String tokenJson = new String(new BigInteger(token, 16).toByteArray());
        return User.from(tokenJson);
    }

    @Override
    public void logout(final HttpServletRequest request, final HttpServletResponse response) {
        CookieUtils.remove(request, response, LOGIN_IDENTITY_KEY);
    }

}