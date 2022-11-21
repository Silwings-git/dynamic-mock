package top.silwings.admin.repository;

import top.silwings.admin.model.User;

/**
 * @ClassName UserRepository
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:37
 * @Since
 **/
public interface UserRepository {
    User findByUserAccount(String userAccount);

}