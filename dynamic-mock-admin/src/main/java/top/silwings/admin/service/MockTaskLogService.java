package top.silwings.admin.service;

import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockTaskLogDto;

import java.util.List;

/**
 * @ClassName MockTaskLogService
 * @Description Mock task 日志
 * @Author Silwings
 * @Date 2022/11/23 23:30
 * @Since
 **/
public interface MockTaskLogService {

    PageData<MockTaskLogDto> query(List<Identity> handlerIdList, String taskCode, String name, PageParam pageParam);

    void delete(List<Identity> handlerIdList, Identity logId);

    MockTaskLogDto find(Identity handlerId, Identity logId);
}