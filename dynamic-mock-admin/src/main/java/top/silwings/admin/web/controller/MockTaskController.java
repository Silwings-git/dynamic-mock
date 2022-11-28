package top.silwings.admin.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.DeleteTaskLogType;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageResult;
import top.silwings.admin.common.Result;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.HandlerInfoDto;
import top.silwings.admin.model.ProjectDto;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.admin.service.MockTaskLogService;
import top.silwings.admin.service.ProjectService;
import top.silwings.admin.web.vo.param.BatchUnregisterParam;
import top.silwings.admin.web.vo.param.DeleteTaskLogParam;
import top.silwings.admin.web.vo.param.QueryTaskLogParam;
import top.silwings.admin.web.vo.param.QueryTaskParam;
import top.silwings.admin.web.vo.param.UnregisterTaskParam;
import top.silwings.admin.web.vo.result.MockTaskLogResult;
import top.silwings.admin.web.vo.result.RunningTaskResult;
import top.silwings.core.common.Identity;
import top.silwings.core.handler.task.AutoCancelTask;
import top.silwings.core.handler.task.MockTaskManager;
import top.silwings.core.model.MockTaskLogDto;
import top.silwings.core.utils.CheckUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName TaskController
 * @Description 定时任务管理
 * @Author Silwings
 * @Date 2022/11/23 20:34
 * @Since
 **/
@Slf4j
@RestController
@RequestMapping("/dynamic-mock/mock/task")
public class MockTaskController {

    private final MockHandlerService mockHandlerService;

    private final MockTaskManager mockTaskManager;

    private final MockTaskLogService mockTaskLogService;

    private final ProjectService projectService;

    public MockTaskController(final MockHandlerService mockHandlerService, final MockTaskManager mockTaskManager, final MockTaskLogService mockTaskLogService, final ProjectService projectService) {
        this.mockHandlerService = mockHandlerService;
        this.mockTaskManager = mockTaskManager;
        this.mockTaskLogService = mockTaskLogService;
        this.projectService = projectService;
    }

    @PostMapping("/running/query")
    @PermissionLimit
    @ApiOperation(value = "查询任务列表")
    public Result<List<RunningTaskResult>> query(@RequestBody final QueryTaskParam param) {

        final List<Identity> handlerIdList = this.getAuthorizedHandlerIds(param.getProjectId(), param.getHandlerId());

        final List<AutoCancelTask> taskList = this.mockTaskManager.query(handlerIdList);

        final List<RunningTaskResult> runningTaskResultList = taskList.stream()
                .map(RunningTaskResult::from)
                .sorted(Comparator.comparing(RunningTaskResult::getTaskCode))
                .collect(Collectors.toList());

        return Result.ok(runningTaskResultList);
    }


    /**
     * 获取已授权的handlerId集
     *
     * @param projectId 项目id
     * @param handlerId 处理器Id
     * @return 已授权的handlerId集
     */
    private List<Identity> getAuthorizedHandlerIds(final Identity projectId, final Identity handlerId) {

        final List<Identity> handlerIdList;

        if (null == projectId && null == handlerId) {
            // 返回全部已授权handlerId
            handlerIdList = UserHolder.getUser().getHandlerIdList();
        } else if (null == projectId) {
            // 验证handlerId,通过仅返回handlerId
            UserHolder.validHandlerId(handlerId);
            handlerIdList = Collections.singletonList(handlerId);
        } else if (null == handlerId) {
            // 验证项目id,返回项目下所有handlerId
            UserHolder.validProjectId(projectId);
            handlerIdList = this.mockHandlerService.queryHandlerIds(projectId);
        } else {
            // 验证项目id,验证指定handlerId是否在项目中存在,返回项目id
            UserHolder.validProjectId(projectId);
            CheckUtils.isIn(handlerId, this.mockHandlerService.queryHandlerIds(projectId), DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "handlerId"));
            handlerIdList = Collections.singletonList(handlerId);
        }

        return handlerIdList;
    }

    @PostMapping("/running/unregister")
    @PermissionLimit
    @ApiOperation(value = "取消任务")
    public Result<Void> unregister(@RequestBody final UnregisterTaskParam param) {

        param.validate();

        UserHolder.validHandlerId(param.getHandlerId());

        this.mockTaskManager.unregisterByTaskCode(param.getHandlerId(), param.getTaskCode(), param.getInterrupt());

        return Result.ok();
    }

    @PostMapping("/running/unregister/batch")
    @PermissionLimit
    @ApiOperation(value = "批量取消任务")
    public Result<Void> batchUnregister(@RequestBody final BatchUnregisterParam param) {

        final List<Identity> unregisterHandlerId;

        if (null == param.getProjectId() && null == param.getHandlerId()) {

            // 取消注册所有已授权handler
            unregisterHandlerId = UserHolder.getUser().getHandlerIdList();

        } else if (null != param.getProjectId() && null == param.getHandlerId()) {

            // 取消指定项目下所有
            UserHolder.validProjectId(param.getProjectId());
            unregisterHandlerId = this.mockHandlerService.queryHandlerIds(param.getProjectId());

        } else if (null != param.getProjectId() && null != param.getHandlerId()) {

            UserHolder.validProjectId(param.getProjectId());
            CheckUtils.isIn(param.getHandlerId(), this.mockHandlerService.queryHandlerIds(param.getProjectId()), DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "handlerId"));
            unregisterHandlerId = Collections.singletonList(param.getHandlerId());

        } else {
            log.error("批量取消注册任务时参数不合规.");
            return Result.ok();
        }

        this.mockTaskManager.unregisterByHandlerIds(unregisterHandlerId, param.getInterrupt());

        return Result.ok();
    }

    @PostMapping("/log/query")
    @PermissionLimit
    @ApiOperation(value = "查询任务日志列表")
    public PageResult<MockTaskLogResult> queryTaskLog(@RequestBody final QueryTaskLogParam param) {

        final List<Identity> handlerIdList = this.getAuthorizedHandlerIds(param.getProjectId(), param.getHandlerId());

        final PageData<MockTaskLogDto> pageData = this.mockTaskLogService.query(handlerIdList, param.getTaskCode(), param.getName(), param);

        final List<MockTaskLogResult> mockTaskLogResultList = pageData.getList().stream()
                .map(MockTaskLogResult::from)
                .collect(Collectors.toList());

        // 补充projectId,projectName,handlerName
        final Map<Identity, HandlerInfoDto> handlerIdNameMap = new HashMap<>();
        final Map<Identity, ProjectDto> projectIdNameMap = new HashMap<>();

        for (final MockTaskLogResult log : mockTaskLogResultList) {

            final HandlerInfoDto handlerInfo = handlerIdNameMap.computeIfAbsent(log.getHandlerId(), this.mockHandlerService::findHandlerInfo);
            if (null == handlerInfo) {
                continue;
            }
            log.setHandlerName(handlerInfo.getName());

            final ProjectDto project = projectIdNameMap.computeIfAbsent(handlerInfo.getProjectId(), this.projectService::find);
            if (null != project) {
                log.setProjectId(project.getProjectId());
                log.setProjectName(project.getProjectName());
            }
        }

        return PageResult.ok(mockTaskLogResultList, pageData.getTotal());
    }

    @PostMapping("/log/del")
    @PermissionLimit
    @ApiOperation(value = "删除任务日志")
    public Result<Void> delTaskLog(@RequestBody final DeleteTaskLogParam param) {

        param.validate();


        final List<Identity> deleteHandlerIdList;

        if (null == param.getProjectId() && null == param.getHandlerId()) {

            // 清理所有已授权handler
            deleteHandlerIdList = UserHolder.getUser().getHandlerIdList();

        } else if (null != param.getProjectId() && null == param.getHandlerId()) {

            // 取消指定项目下所有
            UserHolder.validProjectId(param.getProjectId());
            deleteHandlerIdList = this.mockHandlerService.queryHandlerIds(param.getProjectId());

        } else if (null != param.getProjectId() && null != param.getHandlerId()) {

            UserHolder.validProjectId(param.getProjectId());
            CheckUtils.isIn(param.getHandlerId(), this.mockHandlerService.queryHandlerIds(param.getProjectId()), DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "handlerId"));
            deleteHandlerIdList = Collections.singletonList(param.getHandlerId());

        } else {
            log.error("删除任务日志时参数不合规.");
            return Result.ok();
        }

        this.mockTaskLogService.delete(deleteHandlerIdList, param.getLogId(), DeleteTaskLogType.valueOfCode(param.getDeleteType()));

        return Result.ok();
    }

}