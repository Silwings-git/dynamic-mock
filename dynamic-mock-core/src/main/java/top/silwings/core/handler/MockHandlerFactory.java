package top.silwings.core.handler;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.silwings.core.handler.response.MockResponseInfoFactory;
import top.silwings.core.handler.task.MockTaskInfo;
import top.silwings.core.handler.task.MockTaskInfoFactory;
import top.silwings.core.handler.tree.NodeInterpreter;
import top.silwings.core.repository.definition.MockHandlerDefinition;
import top.silwings.core.utils.ConvertUtils;

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

    private final JsonNodeParser jsonNodeParser;

    private final MockResponseInfoFactory mockResponseInfoFactory;

    private final MockTaskInfoFactory mockTaskInfoFactory;

    public MockHandlerFactory(final JsonNodeParser jsonNodeParser, final MockResponseInfoFactory mockResponseInfoFactory, final MockTaskInfoFactory mockTaskInfoFactory) {
        this.jsonNodeParser = jsonNodeParser;
        this.mockResponseInfoFactory = mockResponseInfoFactory;
        this.mockTaskInfoFactory = mockTaskInfoFactory;
    }

    public MockHandler buildMockHandler(final MockHandlerDefinition definition) {

        // 基本信息
        final MockHandler.MockHandlerBuilder builder = MockHandler.builder();
        builder.id(definition.getId())
                .name(definition.getName())
                .httpMethodList(definition.getHttpMethods().stream().map(method -> HttpMethod.resolve(method.toUpperCase())).collect(Collectors.toList()))
                .requestUri(definition.getRequestUri())
                .delayTime(ConvertUtils.getNoNullOrDefault(definition.getDelayTime(), 0));

        // 自定义空间
        builder.customizeSpaceInterpreter(new NodeInterpreter(this.jsonNodeParser.parse(definition.getCustomizeSpace())));

        // 响应信息
        builder.responseInfoList(
                definition.getResponses().stream()
                        .map(this.mockResponseInfoFactory::buildResponseInfo)
                        .collect(Collectors.toList())
        );

        // Task信息
        final List<MockTaskInfo> mockTaskInfoList = definition.getTasks().stream()
                .map(this.mockTaskInfoFactory::buildTask)
                .collect(Collectors.toList());

        // 同步Task
        builder.syncTaskInfoList(mockTaskInfoList.stream().filter(MockTaskInfo::isSync).collect(Collectors.toList()));

        // 异步Task
        builder.syncTaskInfoList(mockTaskInfoList.stream().filter(MockTaskInfo::isAsync).collect(Collectors.toList()));

        return builder.build();
    }

}