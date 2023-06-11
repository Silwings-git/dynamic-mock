package top.silwings.core.handler;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.ScriptNoSupportException;
import top.silwings.core.handler.plugin.PluginExecutorManager;
import top.silwings.core.handler.plugin.PluginInterfaceType;
import top.silwings.core.handler.plugin.ScriptRegistrationProgram;
import top.silwings.core.handler.plugin.executors.PluginExecutor;
import top.silwings.core.handler.plugin.executors.js.PreMockNashornJSScriptExecutor;
import top.silwings.core.handler.plugin.executors.js.PreResponseNashornJSScriptExecutor;
import top.silwings.core.handler.response.MockResponseInfoFactory;
import top.silwings.core.handler.task.MockTaskInfo;
import top.silwings.core.handler.task.MockTaskInfoFactory;
import top.silwings.core.interpreter.json.JsonTreeParser;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.MockScriptDto;
import top.silwings.core.script.ScriptLanguage;
import top.silwings.core.utils.ConvertUtils;

import java.util.Collections;
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

    private final List<ScriptRegistrationProgram> scriptRegistrationProgram;

    public MockHandlerFactory(final JsonTreeParser jsonTreeParser,
                              final MockResponseInfoFactory mockResponseInfoFactory,
                              final MockTaskInfoFactory mockTaskInfoFactory,
                              final List<ScriptRegistrationProgram> scriptRegistrationProgram) {
        this.jsonTreeParser = jsonTreeParser;
        this.mockResponseInfoFactory = mockResponseInfoFactory;
        this.mockTaskInfoFactory = mockTaskInfoFactory;
        this.scriptRegistrationProgram = ConvertUtils.getNoNullOrDefault(scriptRegistrationProgram, Collections::emptyList);
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

        // 响应信息
        builder.responseInfoList(
                definition.getResponses().stream()
                        .map(this.mockResponseInfoFactory::buildResponseInfo)
                        .collect(Collectors.toList())
        );

        // Task信息
        final List<MockTaskInfo> mockTaskInfoList = definition.getTasks().stream()
                .map(taskDef -> this.mockTaskInfoFactory.buildTask(definition.getHandlerId(), taskDef))
                .collect(Collectors.toList());

        // 同步Task
        builder.syncTaskInfoList(mockTaskInfoList.stream().filter(MockTaskInfo::isSync).collect(Collectors.toList()));

        // 异步Task
        builder.asyncTaskInfoList(mockTaskInfoList.stream().filter(MockTaskInfo::isAsync).collect(Collectors.toList()));

        // 脚本
        builder.pluginExecutorManager(this.buildPluginExecutorManager(definition.getMockScriptList()));

        return builder.build();
    }

    private PluginExecutorManager buildPluginExecutorManager(final List<MockScriptDto> mockScriptList) {

        final PluginExecutorManager manager = new PluginExecutorManager();
        if (CollectionUtils.isNotEmpty(mockScriptList)) {
            mockScriptList.stream()
                    .map(scriptInfo -> {
                        final ScriptLanguage scriptLanguage = scriptInfo.getScriptLanguage();
                        final PluginExecutor<?> pluginExecutor;
                        switch (scriptLanguage) {
                            case JAVA:
                                throw new ScriptNoSupportException("JAVA language is not supported yet.");
                            case JAVA_SCRIPT:
                                if (PluginInterfaceType.PRE_MOCK.equals(scriptInfo.getInterfaceType())) {
                                    pluginExecutor = PreMockNashornJSScriptExecutor.from(scriptInfo.getScriptName(), scriptInfo.getScriptText());
                                } else if (PluginInterfaceType.PRE_RESPONSE.equals(scriptInfo.getInterfaceType())) {
                                    pluginExecutor = PreResponseNashornJSScriptExecutor.from(scriptInfo.getScriptName(), scriptInfo.getScriptText());
                                } else {
                                    throw new IllegalArgumentException();
                                }
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                        return pluginExecutor;
                    })
                    .forEach(manager::register);
        }

        this.scriptRegistrationProgram.forEach(program -> program.register(manager));

        return manager;
    }

}