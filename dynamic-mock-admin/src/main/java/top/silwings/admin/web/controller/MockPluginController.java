package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.Result;
import top.silwings.admin.web.vo.result.MockPluginInfoResult;
import top.silwings.core.handler.plugin.MockPluginInfo;
import top.silwings.core.handler.plugin.PluginRegistrationProgramManager;

import java.util.List;

/**
 * @ClassName MockPluginController
 * @Description
 * @Author Silwings
 * @Date 2023/8/14 18:05
 * @Since
 **/
@RestController
@RequestMapping("/dynamic-mock/plugin")
@Api(value = "Mock 插件管理")
public class MockPluginController {

    private final PluginRegistrationProgramManager pluginRegistrationProgramManager;

    public MockPluginController(final PluginRegistrationProgramManager pluginRegistrationProgramManager) {
        this.pluginRegistrationProgramManager = pluginRegistrationProgramManager;
    }

    @PostMapping("/list")
    @PermissionLimit
    @ApiOperation(value = "获取插件信息列表")
    public Result<List<MockPluginInfoResult>> queryMockPluginInfo() {
        final List<MockPluginInfo> mockPluginInfoList = this.pluginRegistrationProgramManager.getMockPluginInfoList();
        return Result.ok(MockPluginInfoResult.listFrom(mockPluginInfoList));
    }

}