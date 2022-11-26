package top.silwings.admin.service.impl;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.auth.UserAuthInfo;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.UserDto;
import top.silwings.admin.repository.mapper.UserMapper;
import top.silwings.admin.repository.po.UserPo;
import top.silwings.admin.service.UserService;
import top.silwings.admin.utils.EncryptUtils;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;

import java.util.Collections;
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

    private final UserMapper userMapper;

    public UserServiceImpl(final UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Identity create(final String username, final String userAccount, final String password, final int role, final List<Identity> permissionList) {

        final UserPo user = UserPo.builder()
                .username(username)
                .userAccount(userAccount)
                .password(EncryptUtils.encryptPassword(password))
                .role(role)
                .permission(String.join(",", ConvertUtils.getNoEmpty(permissionList, Collections.emptyList(), list -> list.stream().map(Identity::stringValue).collect(Collectors.toList()))))
                .build();
        try {
            this.userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            throw DynamicMockAdminException.from(ErrorCode.USER_DUPLICATE_ACCOUNT);
        }

        return Identity.from(user.getUserId());
    }

    @Override
    public Identity updateById(final Identity userId, final String username, final String password, final int role, final List<Identity> permissionList) {

        final UserPo user = UserPo.builder()
                .username(username)
                .password(ConvertUtils.getNoBlankOrDefault(password, null, EncryptUtils::encryptPassword))
                .role(role)
                .permission(String.join(",", ConvertUtils.getNoEmpty(permissionList, Collections.emptyList(), list -> list.stream().map(Identity::stringValue).collect(Collectors.toList()))))
                .build();

        final Example saveCondition = new Example(UserPo.class);
        saveCondition.createCriteria()
                .andEqualTo(UserPo.C_USER_ID, userId.intValue());

        this.userMapper.updateByConditionSelective(user, saveCondition);

        return userId;
    }

    @Override
    public void changePassword(final String oldPassword, final String newPassword) {

        final UserAuthInfo userAuthInfo = UserHolder.getUser();

        final UserDto user = this.findByUserAccount(userAuthInfo.getUserAccount(), true);

        CheckUtils.isEquals(user.getPassword(), EncryptUtils.encryptPassword(oldPassword), DynamicMockAdminException.supplier(ErrorCode.USER_OLD_PASSWORD_ERROR));

        final UserPo pswUser = UserPo.builder()
                .password(EncryptUtils.encryptPassword(newPassword))
                .build();

        final Example example = new Example(UserPo.class);
        example.createCriteria()
                .andEqualTo(UserPo.C_USER_ID, user.getUserId().intValue());

        this.userMapper.updateByConditionSelective(pswUser, example);
    }

    /**
     * 根据用户账户查询用户信息
     *
     * @param userAccount 用户账户
     * @param required    是否必须存在
     * @return 用户信息.如果require为true却没有查询到将抛出异常
     */
    @Override
    public UserDto findByUserAccount(final String userAccount, final boolean required) {

        CheckUtils.isNotBlank(userAccount, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "userAccount"));

        final Example findCondition = new Example(UserPo.class);
        findCondition.createCriteria()
                .andEqualTo(UserPo.C_USER_ACCOUNT, userAccount);

        final List<UserPo> userPoList = this.userMapper.selectByConditionAndRowBounds(findCondition, new RowBounds(0, 1));
        if (required) {
            CheckUtils.isNotEmpty(userPoList, DynamicMockAdminException.supplier(ErrorCode.USER_NOT_EXIST));
        }

        return ConvertUtils.getNoEmpty(userPoList, null, list -> UserDto.from(list.get(0)));
    }

    @Override
    public void deleteUser(final Identity userId) {

        CheckUtils.isNotEquals(UserHolder.getUserId(), userId, DynamicMockAdminException.supplier(ErrorCode.USER_UPDATE_LOGIN_USER_LIMIT));

        final Example deleteCondition = new Example(UserPo.class);
        deleteCondition.createCriteria()
                .andEqualTo(UserPo.C_USER_ID, userId.intValue());

        this.userMapper.deleteByCondition(deleteCondition);
    }

    @Override
    public PageData<UserDto> query(final String username, final String userAccount, final Integer role, final PageParam pageParam) {

        final Example queryCondition = new Example(UserPo.class);
        queryCondition.createCriteria()
                .andEqualTo(UserPo.C_ROLE, role)
                .andLike(UserPo.C_USERNAME, ConvertUtils.getNoBlankOrDefault(username, null, name -> name + "%"))
                .andLike(UserPo.C_USER_ACCOUNT, ConvertUtils.getNoBlankOrDefault(userAccount, null, account -> account + "%"));

        queryCondition.orderBy(UserPo.C_USER_ACCOUNT).asc();

        final int total = this.userMapper.selectCountByCondition(queryCondition);
        if (total < 0) {
            return PageData.empty();
        }

        final List<UserPo> userPoList = this.userMapper.selectByConditionAndRowBounds(queryCondition, pageParam.toRowBounds());

        final List<UserDto> userList = userPoList.stream()
                .map(UserDto::from)
                .collect(Collectors.toList());

        return PageData.of(userList, total);
    }

}