package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
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
import top.silwings.admin.model.ProjectDto;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.admin.service.ProjectService;
import top.silwings.admin.web.vo.converter.MockHandlerVoConverter;
import top.silwings.admin.web.vo.param.DeleteMockHandlerParam;
import top.silwings.admin.web.vo.param.EnableStatusParam;
import top.silwings.admin.web.vo.param.FindMockHandlerParam;
import top.silwings.admin.web.vo.param.MockHandlerInfoParam;
import top.silwings.admin.web.vo.param.QueryMockHandlerParam;
import top.silwings.admin.web.vo.param.QueryOwnMockHandlerParam;
import top.silwings.admin.web.vo.result.MockHandlerInfoResult;
import top.silwings.admin.web.vo.result.MockHandlerSummaryResult;
import top.silwings.admin.web.vo.result.OwnHandlerInfoResult;
import top.silwings.admin.web.vo.result.QueryOwnHandlerMappingResult;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.QueryConditionDto;
import top.silwings.core.model.validator.MockHandlerValidator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerController
 * @Description 模拟器程序增删改查接口
 * @Author Silwings
 * @Date 2022/11/14 22:10
 * @Since
 **/
@RestController
@RequestMapping("/dynamic-mock/mock/handler")
@Api(value = "Mock 处理器管理")
public class MockHandlerController {

    private final MockHandlerService mockHandlerService;

    private final MockHandlerVoConverter mockHandlerVoConverter;

    private final MockHandlerValidator mockHandlerValidator;

    private final ProjectService projectService;

    public MockHandlerController(final MockHandlerService mockHandlerApplication, final MockHandlerVoConverter mockHandlerVoConverter, final MockHandlerValidator mockHandlerValidator, final ProjectService projectService) {
        this.mockHandlerService = mockHandlerApplication;
        this.mockHandlerVoConverter = mockHandlerVoConverter;
        this.mockHandlerValidator = mockHandlerValidator;
        this.projectService = projectService;
    }

    @PostMapping("/save")
    @PermissionLimit
    @ApiOperation(value = "保存Mock处理器信息")
    public Result<Identity> save(@RequestBody final MockHandlerInfoParam param) {

        param.validate();

        UserHolder.validProjectId(param.getProjectId());

        final MockHandlerDto mockHandlerDto = this.mockHandlerVoConverter.convert(param);

        this.mockHandlerValidator.validate(mockHandlerDto);

        final Identity handlerId;

        if (null == param.getHandlerId()) {
            handlerId = this.mockHandlerService.create(mockHandlerDto);
        } else {
            handlerId = this.mockHandlerService.updateById(mockHandlerDto);
        }

        return Result.ok(handlerId);
    }

    @PostMapping("/find")
    @PermissionLimit
    @ApiOperation(value = "根据id获取Mock处理器信息")
    public Result<MockHandlerInfoResult> find(@RequestBody final FindMockHandlerParam param) {

        param.validate();

        final MockHandlerDto mockHandlerDto = this.mockHandlerService.find(param.getHandlerId());

        UserHolder.validProjectId(mockHandlerDto.getProjectId());

        final ProjectDto projectDto = this.projectService.find(mockHandlerDto.getProjectId());

        final MockHandlerInfoResult mockHandlerInfoVo = this.mockHandlerVoConverter.convert(mockHandlerDto, projectDto);

        return Result.ok(mockHandlerInfoVo);
    }

    @PostMapping("/query")
    @PermissionLimit
    @ApiOperation(value = "分页查询Mock处理器信息")
    public PageResult<MockHandlerSummaryResult> query(@RequestBody QueryMockHandlerParam param) {

        if (null != param.getProjectId()) {
            UserHolder.validProjectId(param.getProjectId());
        }

        final QueryConditionDto queryCondition = QueryConditionDto.builder()
                .projectIdList(UserHolder.getUser().getProjectIdList())
                .projectId(param.getProjectId())
                .name(param.getName())
                .httpMethod(param.getHttpMethod())
                .requestUri(param.getRequestUri())
                .label(param.getLabel())
                .build();

        final PageData<MockHandlerDto> pageData = this.mockHandlerService.query(queryCondition, param);

        final Map<Identity, ProjectDto> projectIdProjectNameMap = pageData.getList().stream()
                .map(MockHandlerDto::getProjectId)
                .distinct()
                .map(this.projectService::find)
                .collect(Collectors.toMap(ProjectDto::getProjectId, Function.identity(), (v1, v2) -> v2));

        final List<MockHandlerSummaryResult> mockHandlerInfoVoList = pageData.getList().stream()
                .map(handler -> this.mockHandlerVoConverter.convertSummary(handler, projectIdProjectNameMap.get(handler.getProjectId())))
                .collect(Collectors.toList());

        return PageResult.ok(mockHandlerInfoVoList, pageData.getTotal());
    }

    @PostMapping("/del")
    @PermissionLimit
    @ApiOperation(value = "根据id删除MOck处理器信息")
    public Result<Void> delete(@RequestBody final DeleteMockHandlerParam param) {

        param.validate();

        UserHolder.validHandlerId(param.getHandlerId());

        this.mockHandlerService.delete(param.getHandlerId());

        return Result.ok();
    }

    @PostMapping("/enableStatus")
    @PermissionLimit
    @ApiOperation(value = "启用/停用Mock处理器")
    public Result<Void> updateEnableStatus(@RequestBody final EnableStatusParam param) {

        param.validate();

        final Identity projectId = this.mockHandlerService.findProjectId(param.getHandlerId());

        UserHolder.validProjectId(projectId);

        this.mockHandlerService.updateEnableStatus(param.getHandlerId(), EnableStatus.valueOfCode(param.getEnableStatus()), this.projectService.find(projectId));

        return Result.ok();
    }

    @PostMapping("/queryOwn")
    @PermissionLimit
    @ApiOperation(value = "查询登录用户的全部Handler信息")
    public Result<QueryOwnHandlerMappingResult> queryOwn(@RequestBody final QueryOwnMockHandlerParam param) {

        // 这里不能将null修改为空集合,空集合表示没有任何项目权限
        final List<Identity> projectIdList;

        if (null == param.getProjectId()) {
            projectIdList = UserHolder.getUser().getProjectIdList();
        } else {
            projectIdList = Collections.singletonList(param.getProjectId());
        }

        final Map<Identity, List<OwnHandlerInfoResult>> projectIdHandlerMap = this.mockHandlerService.queryOwn(projectIdList)
                .stream()
                .map(OwnHandlerInfoResult::from)
                .collect(Collectors.groupingBy(OwnHandlerInfoResult::getProjectId));

        return Result.ok(QueryOwnHandlerMappingResult.builder().projectHandlerMap(projectIdHandlerMap).build());
    }

}