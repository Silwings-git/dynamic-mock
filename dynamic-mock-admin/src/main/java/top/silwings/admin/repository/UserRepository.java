package top.silwings.admin.repository;

import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.model.User;
import top.silwings.core.common.Identity;

/**
 * @ClassName UserRepository
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:37
 * @Since
 **/
public interface UserRepository {
    User findByUserAccount(String userAccount);

    User findById(Identity userId);

    void create(User user);

    boolean delete(Identity userId);

    PageData<User> query(String searchKey, PageParam param);

    void updateById(User user, Identity userId);

}