package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.common.PageResult;
import top.silwings.admin.common.Result;
import top.silwings.admin.service.impl.MockHandlerServiceImpl;
import top.silwings.admin.web.vo.converter.MockHandlerVoConverter;
import top.silwings.admin.web.vo.param.EnableStatusParam;
import top.silwings.admin.web.vo.param.MockHandlerInfoParam;
import top.silwings.admin.web.vo.result.MockHandlerInfoResult;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.model.dto.MockHandlerDto;
import top.silwings.core.model.dto.QueryConditionDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerController
 * @Description 模拟器程序增删改查接口
 * @Author Silwings
 * @Date 2022/11/14 22:10
 * @Since
 **/
@RestController
@RequestMapping("/dynamic/mock/handler")
@Api(value = "Mock 处理器管理")
public class MockHandlerController {

    private final MockHandlerServiceImpl mockHandlerApplication;

    private final MockHandlerVoConverter mockHandlerVoConverter;

    public MockHandlerController(final MockHandlerServiceImpl mockHandlerApplication, final MockHandlerVoConverter mockHandlerVoConverter) {
        this.mockHandlerApplication = mockHandlerApplication;
        this.mockHandlerVoConverter = mockHandlerVoConverter;
    }

    @PostMapping("/save")
    @PermissionLimit
    @ApiOperation(value = "保存Mock处理器信息")
    public Result<Identity> save(@RequestBody final MockHandlerInfoParam mockHandlerInfoParam) {

        final MockHandlerDto mockHandlerDto = this.mockHandlerVoConverter.convert(mockHandlerInfoParam);

        final Identity handlerId = this.mockHandlerApplication.save(mockHandlerDto);

        return Result.ok(handlerId);
    }

    @GetMapping("/find/{handlerId}")
    @PermissionLimit
    @ApiOperation(value = "根据id获取Mock处理器信息")
    public Result<MockHandlerInfoResult> find(@PathVariable("handlerId") final String handlerId) {

        final MockHandlerDto mockHandlerDto = this.mockHandlerApplication.find(Identity.from(handlerId));

        final MockHandlerInfoResult mockHandlerInfoVo = this.mockHandlerVoConverter.convert(mockHandlerDto);

        return Result.ok(mockHandlerInfoVo);
    }

    @GetMapping("/query/{pageNum}/{pageSize}")
    @PermissionLimit
    @ApiOperation(value = "分页查询Mock处理器信息")
    public PageResult<MockHandlerInfoResult> query(@PathVariable("pageNum") final Integer pageNum,
                                                   @PathVariable("pageSize") final Integer pageSize,
                                                   @RequestParam("name") final String name,
                                                   @RequestParam("httpMethod") final String httpMethod,
                                                   @RequestParam("requestUri") final String requestUri,
                                                   @RequestParam("label") final String label) {

        final QueryConditionDto queryCondition = QueryConditionDto.builder()
                .handlerIdList(null)
                .name(name)
                .httpMethod(httpMethod)
                .requestUri(requestUri)
                .label(label)
                .build();

        final PageData<MockHandlerDto> pageData = this.mockHandlerApplication.query(queryCondition, PageParam.of(pageNum, pageSize));

        final List<MockHandlerInfoResult> mockHandlerInfoVoList = pageData.getList().stream()
                .map(this.mockHandlerVoConverter::convert)
                .collect(Collectors.toList());

        return PageResult.ok(mockHandlerInfoVoList, pageData.getTotal());
    }

    @DeleteMapping("/del/{handlerId}")
    @PermissionLimit
    @ApiOperation(value = "根据id删除MOck处理器信息")
    public Result<Void> delete(@PathVariable("handlerId") final String handlerId) {

        this.mockHandlerApplication.delete(Identity.from(handlerId));

        return Result.ok();
    }

    @PostMapping("/enableStatus")
    @PermissionLimit
    @ApiOperation(value = "启用/停用Mock处理器")
    public Result<Void> updateEnableStatus(@RequestBody final EnableStatusParam enableStatusParam) {

        this.mockHandlerApplication.updateEnableStatus(Identity.from(enableStatusParam.getHandlerId()), EnableStatus.valueOf(enableStatusParam.getEnableStatus()));

        return Result.ok();
    }

}