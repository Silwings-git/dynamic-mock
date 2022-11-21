package top.silwings.admin.service;

import top.silwings.admin.model.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName LoginService
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:20
 * @Since
 **/
public interface LoginService {
    String login(String userAccount, String password, final boolean ifRemember, HttpServletResponse response);

    UserDto ifLogin(HttpServletRequest request, HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse response);
}