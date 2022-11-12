package top.silwings.core.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.silwings.core.handler.node.Node;
import top.silwings.core.repository.definition.MockHandlerDefinition;
import top.silwings.core.utils.ConvertUtils;

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

    private final JsonNodeParser jsonNodeParser;

    public MockHandlerFactory(final JsonNodeParser jsonNodeParser) {
        this.jsonNodeParser = jsonNodeParser;
    }

    public MockHandler buildMockHandler(final MockHandlerDefinition definition) {

        // 基本信息
        final MockHandler.MockHandlerBuilder builder = MockHandler.builder();
        builder.id(definition.getId())
                .name(definition.getName())
                .httpMethods(definition.getHttpMethods().stream().map(method -> HttpMethod.resolve(method.toUpperCase())).collect(Collectors.toList()))
                .requestUri(definition.getRequestUri())
                .delayTime(ConvertUtils.getNoNullOrDefault(definition.getDelayTime(), 0))
                .customizeSpace(this.buildCustomizeSpace(definition));

        return null;


    }

    private Map<String, Object> buildCustomizeSpace(final MockHandlerDefinition definition) {

        final Node parse = this.jsonNodeParser.parse(JSON.toJSONString(definition.getCustomizeSpace()));

        // TODO_Silwings: 2022/11/12 这里不执行解释,每一个请求执行一次解释

        // 解析自定义空间时使用空上下文
        final Context emptyContext = Context.builder().build();

        parse.interpret(emptyContext);

        return JSON.parseObject(emptyContext.getJsonStr());
    }


}