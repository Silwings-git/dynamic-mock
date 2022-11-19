package top.silwings.admin.service.impl;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import top.silwings.admin.auth.UserAuthInfo;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.model.User;
import top.silwings.admin.repository.UserRepository;
import top.silwings.admin.service.UserService;
import top.silwings.admin.web.vo.param.ResetPasswordParam;
import top.silwings.core.utils.CheckUtils;

/**
 * @ClassName UserServiceImpl
 * @Description 用户管理实现
 * @Author Silwings
 * @Date 2022/11/19 17:50
 * @Since
 **/
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(final String username, final String userAccount, final String role) {
        try {
            this.userRepository.create(username, userAccount, role);
        } catch (DuplicateKeyException e) {
            throw DynamicMockAdminException.from("Account already exists.");
        }
    }

    @Override
    public void changePassword(final String oldPassword, final String newPassword) {

        final UserAuthInfo userAuthInfo = UserHolder.getUser();

        final User user = this.userRepository.findByUserAccount(userAuthInfo.getUserAccount());

        user.changePassword(oldPassword, newPassword);

        this.userRepository.update(user);
    }

    @Override
    public void deleteUser(final String userAccount) {

        CheckUtils.isNotEquals(UserHolder.getUser().getUserAccount(), userAccount, () -> DynamicMockAdminException.from("You cannot delete your own account."));

        this.userRepository.delete(userAccount);
    }

    @Override
    public PageData<User> query(final String searchKey, final PageParam param) {
        return this.userRepository.query(searchKey, param);
    }

    @Override
    public void resetPassword(final ResetPasswordParam param) {

        final User user = this.userRepository.findByUserAccount(param.getUserAccount());

        user.resetPassword();

        this.userRepository.update(user);
    }
}