package top.silwings.admin.service;

import top.silwings.core.common.Identity;

import java.util.List;

/**
 * @ClassName MockHandlerService
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:19
 * @Since
 **/
public interface MockHandlerService {
    void delete(List<Identity> collect);
}