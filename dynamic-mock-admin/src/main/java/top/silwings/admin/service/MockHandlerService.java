package top.silwings.admin.service;

import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.model.HandlerInfoDto;
import top.silwings.admin.model.MockHandlerSummaryDto;
import top.silwings.admin.model.ProjectDto;
import top.silwings.admin.model.QueryDisableHandlerIdsConditionDto;
import top.silwings.admin.model.QueryEnableHandlerConditionDto;
import top.silwings.admin.model.QueryHandlerConditionDto;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.MockResponseInfoDto;

import java.util.List;

/**
 * @ClassName MockHandlerService
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:19
 * @Since
 **/
public interface MockHandlerService {

    void delete(Identity handlerId);

    MockHandlerDto find(Identity handlerId);

    Identity findProjectId(Identity handlerId);

    PageData<MockHandlerSummaryDto> querySummary(QueryHandlerConditionDto queryCondition, PageParam pageParam);

    void updateEnableStatus(Identity handlerId, EnableStatus enableStatus, ProjectDto project);

    void disableMockHandler(Identity handlerId);

    int findMockHandlerQuantityByProject(Identity projectId);

    Identity create(MockHandlerDto mockHandlerDto);

    Identity updateById(MockHandlerDto mockHandlerDto);

    Identity updateById(MockHandlerDto mockHandlerDto, boolean insertIfAbsent);

    PageData<MockHandlerDto> queryEnableHandlerList(QueryEnableHandlerConditionDto conditionParamDto, PageParam pageParam);

    PageData<Identity> queryDisableHandlerList(QueryDisableHandlerIdsConditionDto conditionParamDto, PageParam pageParam);

    List<Identity> queryHandlerIds(Identity projectId);

    List<Identity> queryHandlerIds(List<Identity> projectIdList);

    List<HandlerInfoDto> queryOwn(List<Identity> projectIdList);

    HandlerInfoDto findHandlerInfo(Identity handlerId);

    void updateMockHandlerResponse(Identity handlerId, MockResponseInfoDto responseInfoDto);

    void updateResponseEnableStatus(Identity handlerId, Identity responseId, EnableStatus enableStatus);

    void updateTaskEnableStatus(Identity handlerId, Identity taskId, EnableStatus enableStatus);
}