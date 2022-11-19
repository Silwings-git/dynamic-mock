package top.silwings.admin.service;

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
}