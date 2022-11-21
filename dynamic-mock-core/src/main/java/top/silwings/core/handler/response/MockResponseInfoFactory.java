package top.silwings.core.handler.response;

import org.springframework.stereotype.Component;
import top.silwings.core.handler.JsonNodeParser;
import top.silwings.core.handler.tree.NodeInterpreter;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.model.MockResponseInfoDto;
import top.silwings.core.utils.ConvertUtils;

import java.util.stream.Collectors;

/**
 * @ClassName MockResponseInfoFactory
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 20:55
 * @Since
 **/
@Component
public class MockResponseInfoFactory {

    private final DynamicValueFactory dynamicValueFactory;

    private final JsonNodeParser jsonNodeParser;

    public MockResponseInfoFactory(final DynamicValueFactory dynamicValueFactory, final JsonNodeParser jsonNodeParser) {
        this.dynamicValueFactory = dynamicValueFactory;
        this.jsonNodeParser = jsonNodeParser;
    }

    public MockResponseInfo buildResponseInfo(final MockResponseInfoDto definition) {
        return MockResponseInfo.builder()
                .name(definition.getName())
                .supportInterpreterList(definition.getSupport().stream().map(this.dynamicValueFactory::buildDynamicValue).map(NodeInterpreter::new).collect(Collectors.toList()))
                .delayTime(ConvertUtils.getNoNullOrDefault(definition.getDelayTime(), 0))
                .responseInterpreter(new NodeInterpreter(this.jsonNodeParser.parse(definition.getResponse())))
                .build();
    }

}