package top.silwings.admin.web.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageResult;
import top.silwings.admin.common.Result;
import top.silwings.admin.common.UnregisterType;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.admin.service.MockTaskLogService;
import top.silwings.admin.web.vo.param.DeleteTaskLogParam;
import top.silwings.admin.web.vo.param.QueryTaskLogParam;
import top.silwings.admin.web.vo.param.QueryTaskParam;
import top.silwings.admin.web.vo.param.UnregisterTaskParam;
import top.silwings.admin.web.vo.result.MockTaskLogResult;
import top.silwings.admin.web.vo.result.TaskResult;
import top.silwings.core.common.Identity;
import top.silwings.core.handler.task.AutoCancelTask;
import top.silwings.core.handler.task.MockTaskManager;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.MockTaskLogDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName TaskController
 * @Description 定时任务管理
 * @Author Silwings
 * @Date 2022/11/23 20:34
 * @Since
 **/
@RestController
@RequestMapping("/dynamic-mock/mock/task")
public class MockTaskController {

    private final MockHandlerService mockHandlerService;

    private final MockTaskManager mockTaskManager;

    private final MockTaskLogService mockTaskLogService;

    public MockTaskController(final MockHandlerService mockHandlerService, final MockTaskManager mockTaskManager, final MockTaskLogService mockTaskLogService) {
        this.mockHandlerService = mockHandlerService;
        this.mockTaskManager = mockTaskManager;
        this.mockTaskLogService = mockTaskLogService;
    }

    @PostMapping("/query")
    @PermissionLimit
    @ApiOperation(value = "查询任务列表")
    public PageResult<TaskResult> query(@RequestBody final QueryTaskParam param) {

        param.validate();

        final Identity projectId = Identity.from(param.getProjectId());

        UserHolder.validPermission(projectId);

        final List<Identity> handlerIdList;

        if (StringUtils.isNotBlank(param.getHandlerId())) {
            final Identity handlerId = Identity.from(param.getHandlerId());
            final MockHandlerDto mockHandler = this.mockHandlerService.find(handlerId);
            UserHolder.validPermission(mockHandler.getProjectId());
            handlerIdList = Collections.singletonList(handlerId);
        } else {
            handlerIdList = this.mockHandlerService.findHandlerIds(projectId);
        }

        final List<AutoCancelTask> taskList = this.mockTaskManager.query(handlerIdList);

        final List<TaskResult> taskResultList = taskList.stream()
                .map(task -> TaskResult.of(task.getTaskCode(), task.getHandlerId(), task.getNumberOfExecute().get(), task.getTaskJson()))
                .collect(Collectors.toList());

        return PageResult.ok(taskResultList, taskResultList.size());
    }

    @PostMapping("/unregister")
    @PermissionLimit
    @ApiOperation(value = "取消任务任务列表")
    public Result<Void> unregister(@RequestBody final UnregisterTaskParam param) {

        param.validate();

        final UnregisterType unregisterType = UnregisterType.valueOfCode(param.getUnregisterType());

        if (UnregisterType.TASK.equals(unregisterType)) {

            this.validPermission(Identity.from(param.getHandlerId()));

            this.mockTaskManager.unregisterByTaskCode(Identity.from(param.getHandlerId()), param.getTaskCode(), param.getInterrupt());

        } else if (UnregisterType.MOCK_HANDLER.equals(unregisterType)) {

            this.validPermission(Identity.from(param.getHandlerId()));

            this.mockTaskManager.unregisterByHandlerId(Identity.from(param.getHandlerId()), param.getInterrupt());

        } else {

            final Identity projectId = Identity.from(param.getProjectId());

            UserHolder.validPermission(projectId);

            this.mockTaskManager.unregisterByHandlerIds(this.mockHandlerService.findHandlerIds(projectId), param.getInterrupt());
        }

        return Result.ok();
    }

    /**
     * 通过handlerId验证是否拥有项目权限
     */
    private void validPermission(final Identity handlerId) {
        final Identity projectId = this.mockHandlerService.findProjectId(handlerId);
        UserHolder.validPermission(projectId);
    }


    @PostMapping("/log/query")
    @PermissionLimit
    @ApiOperation(value = "查询任务日志列表")
    public PageResult<MockTaskLogResult> queryTaskLog(@RequestBody final QueryTaskLogParam param) {

        param.validate();

        final Identity handlerId = Identity.from(param.getHandlerId());

        this.validPermission(handlerId);

        final PageData<MockTaskLogDto> pageData = this.mockTaskLogService.query(handlerId, param.getName(), param);

        final List<MockTaskLogResult> MockTaskLogResultList = pageData.getList().stream()
                .map(MockTaskLogResult::from)
                .collect(Collectors.toList());

        return PageResult.ok(MockTaskLogResultList, pageData.getTotal());
    }

    @PostMapping("/log/del")
    @PermissionLimit
    @ApiOperation(value = "删除任务日志")
    public Result<Void> delTaskLog(@RequestBody final DeleteTaskLogParam param) {

        param.validate();

        final Identity handlerId = Identity.from(param.getHandlerId());

        this.validPermission(handlerId);

        this.mockTaskLogService.delete(handlerId, Identity.from(param.getLogId()));

        return Result.ok();
    }

}