package top.silwings.core.handler;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.handler.plugin.interceptor.MockInterceptorContext;
import top.silwings.core.handler.plugin.interceptor.MockInterceptorContextFactory;
import top.silwings.core.handler.plugin.interceptor.PreMockInterceptor;
import top.silwings.core.handler.plugin.interceptor.PreResponseInterceptor;
import top.silwings.core.handler.plugin.interceptor.ScriptAdviceInterceptor;
import top.silwings.core.handler.plugin.script.ScriptInterfaceType;
import top.silwings.core.handler.plugin.script.mapping.ScriptMappingFactory;
import top.silwings.core.handler.response.MockResponseInfoFactory;
import top.silwings.core.handler.task.MockTaskInfo;
import top.silwings.core.handler.task.MockTaskInfoFactory;
import top.silwings.core.interpreter.json.JsonTreeParser;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.MockScriptDto;

import java.util.List;
import java.util.Map;
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

    private final MockInterceptorContextFactory mockInterceptorContextFactory;

    private final ScriptMappingFactory scriptMappingFactory;

    public MockHandlerFactory(final JsonTreeParser jsonTreeParser, final MockResponseInfoFactory mockResponseInfoFactory, final MockTaskInfoFactory mockTaskInfoFactory, final MockInterceptorContextFactory mockInterceptorContextFactory, final ScriptMappingFactory scriptMappingFactory) {
        this.jsonTreeParser = jsonTreeParser;
        this.mockResponseInfoFactory = mockResponseInfoFactory;
        this.mockTaskInfoFactory = mockTaskInfoFactory;
        this.mockInterceptorContextFactory = mockInterceptorContextFactory;
        this.scriptMappingFactory = scriptMappingFactory;
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

        // 拦截器
        builder.mockInterceptorContext(this.buildMockInterceptorContext(definition.getMockScriptList()));

        return builder.build();
    }

    private MockInterceptorContext buildMockInterceptorContext(final List<MockScriptDto> mockScriptList) {

        if (CollectionUtils.isEmpty(mockScriptList)) {
            return MockInterceptorContext.empty();
        }

        final MockInterceptorContextFactory.Builder builder = this.mockInterceptorContextFactory.builder();

        final Map<ScriptInterfaceType, List<MockScriptDto>> interfaceScriptMap
                = mockScriptList.stream().collect(Collectors.groupingBy(MockScriptDto::getScriptInterfaceType));

        interfaceScriptMap.forEach((interfaceType, value) -> value.forEach(script -> {
            switch (interfaceType) {
                case PRE_MOCK_INTERCEPTOR:
                    final PreMockInterceptor preMockInterceptor = this.scriptMappingFactory.mapping(script.getScriptLanguageType(), PreMockInterceptor.class, script.getScriptText());
                    // 使用能够处理脚本执行失败的拦截器包装脚本
                    builder.register(ScriptAdviceInterceptor.from(preMockInterceptor));
                    break;
                case PRE_RESPONSE_INTERCEPTOR:
                    final PreResponseInterceptor responseInterceptor = this.scriptMappingFactory.mapping(script.getScriptLanguageType(), PreResponseInterceptor.class, script.getScriptText());
                    // 使用能够处理脚本执行失败的拦截器包装脚本
                    builder.register(ScriptAdviceInterceptor.from(responseInterceptor));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }));

        return builder.build();
    }

}