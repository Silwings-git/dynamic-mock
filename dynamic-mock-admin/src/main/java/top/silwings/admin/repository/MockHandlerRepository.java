package top.silwings.admin.repository;

import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.model.dto.MockHandlerDto;
import top.silwings.core.model.dto.QueryConditionDto;

import java.util.List;

/**
 * @ClassName MockHandlerMySqlRepository
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 11:57
 * @Since
 **/
public interface MockHandlerRepository {

    Identity create(MockHandlerDto mockHandlerDto);

    Identity update(MockHandlerDto mockHandlerDto);

    MockHandlerDto find(Identity handlerId);

    PageData<MockHandlerDto> query(QueryConditionDto queryCondition, PageParam pageParam);

    void delete(Identity handlerId);

    void delete(List<Identity> handlerIdList);

    void updateEnableStatus(Identity handlerId, EnableStatus enableStatus);

    PageData<MockHandlerDto> queryEnableHandlerList(PageParam pageParam);

}