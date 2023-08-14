package top.silwings.core.handler;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.exceptions.NoMatchingPluginRegistrationProgramException;
import top.silwings.core.handler.check.CheckInfoFactory;
import top.silwings.core.handler.plugin.PluginExecutorManager;
import top.silwings.core.handler.plugin.PluginRegistrationProgramManager;
import top.silwings.core.handler.response.MockResponseInfoFactory;
import top.silwings.core.handler.task.MockTaskInfo;
import top.silwings.core.handler.task.MockTaskInfoFactory;
import top.silwings.core.interpreter.json.JsonTreeParser;
import top.silwings.core.model.MockHandlerDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerFactory
 * @Description MockHandler工厂
 * @Author Silwings
 * @Date 2022/11/10 22:36
 * @Since
 **/
@Component
public class MockHandlerFactory {

    private final JsonTreeParser jsonTreeParser;

    private final MockResponseInfoFactory mockResponseInfoFactory;

    private final MockTaskInfoFactory mockTaskInfoFactory;

    private final CheckInfoFactory checkInfoFactory;

    private final PluginRegistrationProgramManager pluginRegistrationProgramManager;

    public MockHandlerFactory(final JsonTreeParser jsonTreeParser,
                              final MockResponseInfoFactory mockResponseInfoFactory,
                              final MockTaskInfoFactory mockTaskInfoFactory,
                              final CheckInfoFactory checkInfoFactory,
                              final PluginRegistrationProgramManager pluginRegistrationProgramManager) {
        this.jsonTreeParser = jsonTreeParser;
        this.mockResponseInfoFactory = mockResponseInfoFactory;
        this.mockTaskInfoFactory = mockTaskInfoFactory;
        this.checkInfoFactory = checkInfoFactory;
        this.pluginRegistrationProgramManager = pluginRegistrationProgramManager;
    }

    public MockHandler buildMockHandler(final MockHandlerDto definition) {

        // 基本信息
        final MockHandler.MockHandlerBuilder builder = MockHandler.builder();
        builder.handlerId(definition.getHandlerId())
                .name(definition.getName())
                .httpMethodList(definition.getHttpMethods())
                .requestUri(definition.getRequestUri())
                .delayTime(definition.getDelayTime())
                .version(definition.getVersion());

        // 自定义空间.
        builder.customizeSpaceInterpreter(new CustomizeSpaceInterpreter(definition.getCustomizeSpace(), this.jsonTreeParser.parse(definition.getCustomizeSpace())));

        // 校验
        builder.checkInfo(this.checkInfoFactory.buildCheck(definition.getCheckInfo()));

        // 响应信息
        builder.responseInfoList(
                definition.getResponses().stream()
                        .filter(responseInfo -> EnableStatus.ENABLE.equals(responseInfo.getEnableStatus()))
                        .map(this.mockResponseInfoFactory::buildResponseInfo)
                        .collect(Collectors.toList())
        );

        // Task信息
        final List<MockTaskInfo> mockTaskInfoList = definition.getTasks().stream()
                .filter(taskDef -> EnableStatus.ENABLE.equals(taskDef.getEnableStatus()))
                .map(taskDef -> this.mockTaskInfoFactory.buildTask(definition.getHandlerId(), taskDef))
                .collect(Collectors.toList());

        // 同步Task
        builder.syncTaskInfoList(mockTaskInfoList.stream().filter(MockTaskInfo::isSync).collect(Collectors.toList()));

        // 异步Task
        builder.asyncTaskInfoList(mockTaskInfoList.stream().filter(MockTaskInfo::isAsync).collect(Collectors.toList()));

        // 插件
        builder.pluginExecutorManager(this.buildPluginExecutorManager(definition));

        return builder.build();
    }

    private PluginExecutorManager buildPluginExecutorManager(final MockHandlerDto definition) {

        final PluginExecutorManager manager = new PluginExecutorManager();

        if (CollectionUtils.isNotEmpty(definition.getPlugins())) {
            definition.getPlugins()
                    .stream()
                    .filter(plugin -> EnableStatus.ENABLE.equals(plugin.getEnableStatus()))
                    .map(this.pluginRegistrationProgramManager::findPluginRegistrationProgram)
                    .forEach(programInfo -> {
                        if (null == programInfo.getPluginRegistrationProgram()) {
                            throw new NoMatchingPluginRegistrationProgramException(programInfo.getMockHandlerPluginInfo().getPluginCode());
                        }
                        programInfo.getPluginRegistrationProgram().register(programInfo.getMockHandlerPluginInfo(), definition, manager);
                    });
        }

        return manager;
    }

}