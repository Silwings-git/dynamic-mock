package top.silwings.core.repository;

import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.common.PageData;
import top.silwings.core.common.PageParam;
import top.silwings.core.repository.dto.MockHandlerDto;
import top.silwings.core.repository.dto.QueryConditionDto;

/**
 * @ClassName MockHandlerRepository
 * @Description MockHandler定义存储库
 * @Author Silwings
 * @Date 2022/11/9 22:06
 * @Since
 **/
public interface MockHandlerRepository {
    Identity create(MockHandlerDto mockHandlerDto);

    Identity update(MockHandlerDto mockHandlerDto);

    MockHandlerDto find(Identity handlerId);

    PageData<MockHandlerDto> query(QueryConditionDto queryCondition, PageParam pageParam);

    void delete(Identity handlerId);

    void updateEnableStatus(Identity handlerId, EnableStatus enableStatus);
}