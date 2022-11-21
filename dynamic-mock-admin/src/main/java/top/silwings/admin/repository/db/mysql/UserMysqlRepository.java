package top.silwings.admin.repository.db.mysql;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.model.User;
import top.silwings.admin.repository.UserRepository;
import top.silwings.admin.repository.db.mysql.mapper.UserMapper;
import top.silwings.admin.repository.db.mysql.po.UserPo;

import java.util.List;

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
}