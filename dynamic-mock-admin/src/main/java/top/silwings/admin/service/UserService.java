package top.silwings.admin.service;

import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.model.User;
import top.silwings.admin.web.vo.param.ResetPasswordParam;

/**
 * @ClassName UserService
 * @Description 用户管理
 * @Author Silwings
 * @Date 2022/11/19 17:50
 * @Since
 **/
public interface UserService {
    void createUser(String username, String userAccount, String role);

    void changePassword(String oldPassword, String newPassword);

    void deleteUser(String userAccount);

    PageData<User> query(String searchKey, PageParam param);

    void resetPassword(ResetPasswordParam param);
}