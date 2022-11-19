package top.silwings.admin.service;

import top.silwings.admin.model.User;

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
    String login(String userAccount, String password, HttpServletResponse response);

    User ifLogin(HttpServletRequest request, HttpServletResponse response);
}