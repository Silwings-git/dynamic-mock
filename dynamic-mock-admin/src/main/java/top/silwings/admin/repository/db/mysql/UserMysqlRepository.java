package top.silwings.admin.repository.db.mysql;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.model.User;
import top.silwings.admin.repository.UserRepository;
import top.silwings.admin.repository.db.mysql.mapper.UserMapper;
import top.silwings.admin.repository.db.mysql.po.UserPo;
import top.silwings.core.common.Identity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName UserMysqlRepository
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:37
 * @Since
 **/
@Component
public class UserMysqlRepository implements UserRepository {

    private final UserMapper userMapper;

    public UserMysqlRepository(final UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public User findByUserAccount(final String userAccount) {

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

        return User.from(userPoList.get(0));
    }

    @Override
    public User findById(final Identity userId) {

        if (null == userId) {
            return null;
        }

        final Example findCondition = new Example(UserPo.class);
        findCondition.createCriteria()
                .andEqualTo(UserPo.C_USER_ID, userId.longValue());

        final List<UserPo> userPoList = this.userMapper.selectByConditionAndRowBounds(findCondition, new RowBounds(0, 1));
        if (CollectionUtils.isEmpty(userPoList)) {
            return null;
        }

        return User.from(userPoList.get(0));
    }

    @Override
    public void create(final User user) {
        this.userMapper.insertSelective(user.toUser());
    }

    @Override
    public void updateById(final User user, final Identity userId) {
        final Example saveCondition = new Example(UserPo.class);
        saveCondition.createCriteria()
                .andEqualTo(UserPo.C_USER_ID, userId.longValue());

        this.userMapper.updateByConditionSelective(user.toUser(), saveCondition);
    }

    @Override
    public void delete(final Identity userId) {

        final Example deleteCondition = new Example(UserPo.class);
        deleteCondition.createCriteria()
                .andEqualTo(UserPo.C_USER_ID, userId.longValue());

        this.userMapper.deleteByCondition(deleteCondition);
    }

    @Override
    public PageData<User> query(final String searchKey, final PageParam param) {

        final Example queryCondition = new Example(UserPo.class);

        if (StringUtils.isNotBlank(searchKey)) {
            queryCondition.createCriteria()
                    .andLike(UserPo.C_USER_ACCOUNT, "%".concat(searchKey))
                    .orLike(UserPo.C_USERNAME, "%".concat(searchKey));
        }

        final int total = this.userMapper.selectCountByCondition(queryCondition);
        if (total < 0) {
            return PageData.empty();
        }

        final List<UserPo> userPoList = this.userMapper.selectByConditionAndRowBounds(queryCondition, param.toRowBounds());

        final List<User> userList = userPoList.stream()
                .map(User::from)
                .collect(Collectors.toList());

        return PageData.of(userList, total);
    }
}