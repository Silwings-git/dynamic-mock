package top.silwings.admin.service.impl;

import org.springframework.stereotype.Service;
import top.silwings.admin.auth.UserAuthInfo;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.model.User;
import top.silwings.admin.repository.UserRepository;
import top.silwings.admin.service.UserService;

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

        this.userRepository.create(username, userAccount, role);
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
        this.userRepository.delete(userAccount);
    }
}