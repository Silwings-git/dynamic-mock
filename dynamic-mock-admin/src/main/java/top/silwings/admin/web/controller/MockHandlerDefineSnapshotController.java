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
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.MockHandlerDefineSnapshotDto;
import top.silwings.admin.repository.MockHandlerDefineSnapshotRepository;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.admin.web.vo.converter.MockHandlerDefineSnapshotVoConverter;
import top.silwings.admin.web.vo.param.FindMockHandlerSnapshotParam;
import top.silwings.admin.web.vo.param.QueryMockHandlerSnapshotParam;
import top.silwings.admin.web.vo.param.RecoverMockHandlerSnapshotParam;
import top.silwings.admin.web.vo.result.MockHandlerDefineSnapshotResult;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockHandlerDto;

import java.util.Collections;
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
@RequestMapping("/dynamic-mock/snapshot/handler")
@Api(value = "Mock 处理器管理")
public class MockHandlerDefineSnapshotController {

    private final MockHandlerDefineSnapshotRepository mockHandlerDefineSnapshotRepository;

    private final MockHandlerDefineSnapshotVoConverter mockHandlerDefineSnapshotVoConverter;

    private final MockHandlerService mockHandlerService;

    public MockHandlerDefineSnapshotController(final MockHandlerDefineSnapshotRepository mockHandlerDefineSnapshotRepository, final MockHandlerDefineSnapshotVoConverter mockHandlerDefineSnapshotVoConverter, final MockHandlerService mockHandlerService) {
        this.mockHandlerDefineSnapshotRepository = mockHandlerDefineSnapshotRepository;
        this.mockHandlerDefineSnapshotVoConverter = mockHandlerDefineSnapshotVoConverter;
        this.mockHandlerService = mockHandlerService;
    }

    @PostMapping("/find")
    @PermissionLimit
    @ApiOperation(value = "根据id获取Mock处理器快照信息")
    public Result<MockHandlerDefineSnapshotResult> find(@RequestBody final FindMockHandlerSnapshotParam param) {

        param.validate();

        final MockHandlerDefineSnapshotDto snapshotDto = this.mockHandlerDefineSnapshotRepository.findLast(param.getSnapshotVersion());

        this.checkSnapshotPermissions(snapshotDto);

        return Result.ok(this.mockHandlerDefineSnapshotVoConverter.convert(snapshotDto));
    }

    private void checkSnapshotPermissions(final MockHandlerDefineSnapshotDto snapshotDto) {
        if (null != snapshotDto) {
            final MockHandlerDto mockHandlerDto = snapshotDto.getMockHandlerDto();
            if (null != mockHandlerDto) {
                UserHolder.validProjectId(mockHandlerDto.getProjectId());
            }
        }
    }

    @PostMapping("/query")
    @PermissionLimit
    @ApiOperation(value = "分页查询Mock处理器定义快照信息")
    public PageResult<MockHandlerDefineSnapshotResult> query(@RequestBody QueryMockHandlerSnapshotParam param) {

        final Identity handlerId = param.getHandlerId();
        if (null != handlerId) {
            UserHolder.validHandlerId(handlerId);
        }

        final PageData<MockHandlerDefineSnapshotDto> pageData = this.mockHandlerDefineSnapshotRepository.query(null == handlerId
                ? UserHolder.getUser().getHandlerIdList()
                : Collections.singletonList(handlerId), param);

        final List<MockHandlerDefineSnapshotResult> resList = pageData.getList()
                .stream()
                .map(this.mockHandlerDefineSnapshotVoConverter::convert)
                .collect(Collectors.toList());

        return PageResult.ok(resList, pageData.getTotal());
    }

    @PostMapping("/recover")
    @PermissionLimit
    @ApiOperation(value = "恢复快照")
    public Result<Identity> recover(@RequestBody RecoverMockHandlerSnapshotParam param) {

        param.validate();

        final MockHandlerDefineSnapshotDto snapshotDto = this.mockHandlerDefineSnapshotRepository.findLast(param.getSnapshotVersion());

        if (null == snapshotDto) {
            throw DynamicMockAdminException.from(ErrorCode.MOCK_HANDLER_DEFINE_SNAPSHOT_NOT_EXIST);
        }

        this.checkSnapshotPermissions(snapshotDto);

        final Identity handlerId = this.mockHandlerService.updateById(snapshotDto.getMockHandlerDto(), true);

        return Result.ok(handlerId);
    }

}