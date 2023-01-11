package top.silwings.core.handler;

import org.springframework.stereotype.Component;
import top.silwings.core.handler.response.MockResponseInfoFactory;
import top.silwings.core.handler.task.MockTaskInfo;
import top.silwings.core.handler.task.MockTaskInfoFactory;
import top.silwings.core.interpreter.CustomizeSpaceInterpreter;
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

    public MockHandlerFactory(final JsonTreeParser jsonTreeParser, final MockResponseInfoFactory mockResponseInfoFactory, final MockTaskInfoFactory mockTaskInfoFactory) {
        this.jsonTreeParser = jsonTreeParser;
        this.mockResponseInfoFactory = mockResponseInfoFactory;
        this.mockTaskInfoFactory = mockTaskInfoFactory;
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

        return builder.build();
    }

}