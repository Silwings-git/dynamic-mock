package top.silwings.admin.web.controller;

import io.swagger.annotations.ApiOperation;
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
import top.silwings.admin.web.vo.result.RunningTaskResult;
import top.silwings.core.common.Identity;
import top.silwings.core.handler.task.AutoCancelTask;
import top.silwings.core.handler.task.MockTaskManager;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.MockTaskLogDto;

import java.util.Collections;
import java.util.Comparator;
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

    @PostMapping("/running/query")
    @PermissionLimit
    @ApiOperation(value = "查询任务列表")
    public Result<List<RunningTaskResult>> query(@RequestBody final QueryTaskParam param) {

        param.validate();

        UserHolder.validPermission(param.getProjectId());

        final List<Identity> handlerIdList;

        if (null != param.getHandlerId()) {

            final MockHandlerDto mockHandler = this.mockHandlerService.find(param.getHandlerId());
            UserHolder.validPermission(mockHandler.getProjectId());
            handlerIdList = Collections.singletonList(param.getHandlerId());

        } else {
            handlerIdList = this.mockHandlerService.findHandlerIds(param.getProjectId());
        }

        final List<AutoCancelTask> taskList = this.mockTaskManager.query(handlerIdList);

        final List<RunningTaskResult> runningTaskResultList = taskList.stream()
                .map(RunningTaskResult::from)
                .sorted(Comparator.comparing(RunningTaskResult::getTaskCode))
                .collect(Collectors.toList());

        return Result.ok(runningTaskResultList);
    }

    @PostMapping("/running/unregister")
    @PermissionLimit
    @ApiOperation(value = "取消任务")
    public Result<Void> unregister(@RequestBody final UnregisterTaskParam param) {

        param.validate();

        final UnregisterType unregisterType = UnregisterType.valueOfCode(param.getUnregisterType());

        if (UnregisterType.TASK.equals(unregisterType)) {

            this.validPermission(param.getHandlerId());

            this.mockTaskManager.unregisterByTaskCode(param.getHandlerId(), param.getTaskCode(), param.getInterrupt());

        } else if (UnregisterType.MOCK_HANDLER.equals(unregisterType)) {

            this.validPermission(param.getHandlerId());

            this.mockTaskManager.unregisterByHandlerId(param.getHandlerId(), param.getInterrupt());

        } else {

            UserHolder.validPermission(param.getProjectId());

            this.mockTaskManager.unregisterByHandlerIds(this.mockHandlerService.findHandlerIds(param.getProjectId()), param.getInterrupt());
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

        this.validPermission(param.getHandlerId());

        final PageData<MockTaskLogDto> pageData = this.mockTaskLogService.query(param.getHandlerId(), param.getTaskCode(), param.getName(), param);

        final List<MockTaskLogResult> mockTaskLogResultList = pageData.getList().stream()
                .map(MockTaskLogResult::from)
                .collect(Collectors.toList());

        return PageResult.ok(mockTaskLogResultList, pageData.getTotal());
    }

    @PostMapping("/log/del")
    @PermissionLimit
    @ApiOperation(value = "删除任务日志")
    public Result<Void> delTaskLog(@RequestBody final DeleteTaskLogParam param) {

        param.validate();

        this.validPermission(param.getHandlerId());

        this.mockTaskLogService.delete(param.getHandlerId(), param.getLogId());

        return Result.ok();
    }

}