package top.silwings.admin.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.auth.UserAuthInfo;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.model.User;
import top.silwings.admin.repository.UserRepository;
import top.silwings.admin.repository.db.mysql.mapper.UserMapper;
import top.silwings.admin.repository.db.mysql.po.UserPo;
import top.silwings.admin.service.UserService;
import top.silwings.admin.utils.EncryptUtils;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    private final UserMapper userMapper;


    private final ApplicationEventPublisher applicationEventPublisher;

    public UserServiceImpl(final UserRepository userRepository, final UserMapper userMapper, final ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void create(final String username, final String userAccount, final String password, final int role) {

        final UserPo user = UserPo.builder()
                .username(username)
                .userAccount(userAccount)
                .password(EncryptUtils.encryptPassword(password))
                .role(role)
                .build();
        try {
            this.userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            throw DynamicMockAdminException.from("Account already exists.");
        }
    }

    @Override
    public void updateById(final Identity userId, final String username, final String password, final int role) {

        final UserPo user = UserPo.builder()
                .username(username)
                .password(ConvertUtils.getNoBlankOrDefault(password, null, EncryptUtils::encryptPassword))
                .role(role)
                .build();

        final Example saveCondition = new Example(UserPo.class);
        saveCondition.createCriteria()
                .andEqualTo(UserPo.C_USER_ID, userId.intValue());

        this.userMapper.updateByConditionSelective(user, saveCondition);
    }

    @Override
    public void changePassword(final String oldPassword, final String newPassword) {

        final UserAuthInfo userAuthInfo = UserHolder.getUser();

        final UserPo user = this.findByUserAccount(userAuthInfo.getUserAccount());

        CheckUtils.isNotNull(user, () -> DynamicMockAdminException.from("The user account does not exist."));

        CheckUtils.isEquals(user.getPassword(), EncryptUtils.encryptPassword(oldPassword), () -> DynamicMockAdminException.from("Original password error."));

        final UserPo pswUser = UserPo.builder()
                .password(EncryptUtils.encryptPassword(newPassword))
                .build();

        final Example example = new Example(UserPo.class);
        example.createCriteria()
                .andEqualTo(UserPo.C_USER_ID, user.getUserId());

        this.userMapper.updateByConditionSelective(pswUser, example);
    }

    private UserPo findByUserAccount(final String userAccount) {

        if (StringUtils.isBlank(userAccount)) {
            return null;
        }

        final Example findCondition = new Example(UserPo.class);
        findCondition.createCriteria()
                .andEqualTo(UserPo.C_USER_ACCOUNT, userAccount);

        final List<UserPo> userPoList = this.userMapper.selectByConditionAndRowBounds(findCondition, new RowBounds(0, 1));
        if (CollectionUtils.isEmpty(userPoList)) {
            return null;
        }

        return userPoList.get(0);
    }

    @Override
    public void deleteUser(final Identity userId) {

        CheckUtils.isEquals(UserHolder.getUserId(), userId, () -> DynamicMockAdminException.from("You cannot delete your own account."));

        final Example deleteCondition = new Example(UserPo.class);
        deleteCondition.createCriteria()
                .andEqualTo(UserPo.C_USER_ID, userId.intValue());

        this.userMapper.deleteByCondition(deleteCondition);
    }

    @Override
    public PageData<User> query(final String username, final String userAccount, final Integer role, final PageParam pageParam) {

        final Example queryCondition = new Example(UserPo.class);
        queryCondition.createCriteria()
                .andEqualTo(UserPo.C_ROLE, role)
                .andLike(UserPo.C_USERNAME, ConvertUtils.getNoBlankOrDefault(username, null, name -> name + "%"))
                .andLike(UserPo.C_USER_ACCOUNT, ConvertUtils.getNoBlankOrDefault(userAccount, null, account -> account + "%"));

        final int total = this.userMapper.selectCountByCondition(queryCondition);
        if (total < 0) {
            return PageData.empty();
        }

        final List<UserPo> userPoList = this.userMapper.selectByConditionAndRowBounds(queryCondition, pageParam.toRowBounds());

        final List<User> userList = userPoList.stream()
                .map(User::from)
                .collect(Collectors.toList());

        return PageData.of(userList, total);
    }

}