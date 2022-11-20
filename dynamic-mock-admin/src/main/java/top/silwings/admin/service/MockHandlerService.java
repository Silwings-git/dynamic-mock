package top.silwings.admin.service;

import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.model.dto.MockHandlerDto;
import top.silwings.core.model.dto.QueryConditionDto;

/**
 * @ClassName MockHandlerService
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:19
 * @Since
 **/
public interface MockHandlerService {
    void delete(Identity handlerId);

    Identity save(MockHandlerDto mockHandlerDto, Identity projectId);

    MockHandlerDto find(Identity handlerId);

    PageData<MockHandlerDto> query(QueryConditionDto queryCondition, PageParam pageParam);

    void updateEnableStatus(Identity handlerId, EnableStatus enableStatus);
}