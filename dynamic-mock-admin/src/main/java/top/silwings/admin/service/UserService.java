package top.silwings.admin.service;

import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.model.User;
import top.silwings.core.common.Identity;

/**
 * @ClassName UserService
 * @Description 用户管理
 * @Author Silwings
 * @Date 2022/11/19 17:50
 * @Since
 **/
public interface UserService {
    void create(String username, String userAccount, String password, int role);

    void updateById(Identity userId, String username, String password, int role);

    void changePassword(String oldPassword, String newPassword);

    void deleteUser(Identity userId);

    PageData<User> query(String searchKey, Integer role, PageParam pageParam);

}