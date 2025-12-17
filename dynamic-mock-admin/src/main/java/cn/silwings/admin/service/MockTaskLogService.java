package cn.silwings.admin.service;

import cn.silwings.admin.common.DeleteTaskLogType;
import cn.silwings.admin.common.PageData;
import cn.silwings.admin.common.PageParam;
import cn.silwings.core.common.Identity;
import cn.silwings.core.model.MockTaskLogDto;

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

    void delete(List<Identity> handlerIdList, Identity logId, final DeleteTaskLogType type);

    MockTaskLogDto find(Identity handlerId, Identity logId);
}