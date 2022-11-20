package top.silwings.admin.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import top.silwings.admin.auth.UserAuthInfo;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.events.DeleteUserEvent;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.model.User;
import top.silwings.admin.repository.UserRepository;
import top.silwings.admin.service.UserService;
import top.silwings.admin.utils.EncryptUtils;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;

import java.beans.Transient;

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

    private final ApplicationEventPublisher applicationEventPublisher;

    public UserServiceImpl(final UserRepository userRepository, final ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void create(final String username, final String userAccount, final String password, final int role) {

        final User user = User.builder()
                .username(username)
                .userAccount(userAccount)
                .password(EncryptUtils.encryptPassword(password))
                .role(role)
                .build();
        try {
            this.userRepository.create(user);
        } catch (DuplicateKeyException e) {
            throw DynamicMockAdminException.from("Account already exists.");
        }
    }

    @Override
    public void update(final Identity userId, final String username, final String password, final int role) {
        final User user = User.builder()
                .username(username)
                .password(ConvertUtils.getNoBlankOrDefault(password, null, EncryptUtils::encryptPassword))
                .role(role)
                .build();

        this.userRepository.updateById(user, userId);
    }

    @Override
    public void changePassword(final String oldPassword, final String newPassword) {

        final UserAuthInfo userAuthInfo = UserHolder.getUser();

        final User user = this.userRepository.findByUserAccount(userAuthInfo.getUserAccount());

        CheckUtils.isEquals(user.getPassword(), EncryptUtils.encryptPassword(oldPassword), () -> DynamicMockAdminException.from("Original password error."));

        final User newUser = User.builder()
                .password(EncryptUtils.encryptPassword(newPassword))
                .build();

        this.userRepository.updateById(newUser, user.getUserId());
    }

    @Override
    @Transient
    public void deleteUser(final Identity userId) {

        CheckUtils.isEquals(UserHolder.getUserId(), userId, () -> DynamicMockAdminException.from("You cannot delete your own account."));

        final User user = this.userRepository.findById(userId);
        if (null == user) {
            return;
        }

        if (this.userRepository.delete(userId)) {
            this.applicationEventPublisher.publishEvent(DeleteUserEvent.of(this, user));
        }
    }

    @Override
    public PageData<User> query(final String searchKey, final PageParam param) {
        return this.userRepository.query(searchKey, param);
    }

}