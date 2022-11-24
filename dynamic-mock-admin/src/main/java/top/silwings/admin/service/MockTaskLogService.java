package top.silwings.admin.service;

import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockTaskLogDto;

/**
 * @ClassName MockTaskLogService
 * @Description Mock task 日志
 * @Author Silwings
 * @Date 2022/11/23 23:30
 * @Since
 **/
public interface MockTaskLogService {
    Identity create(MockTaskLogDto mockTaskLog);

    Identity updateByLogId(MockTaskLogDto mockTaskLog);

    PageData<MockTaskLogDto> query(Identity handlerId, String name, PageParam pageParam);

    void delete(Identity handlerId, Identity logId);
}