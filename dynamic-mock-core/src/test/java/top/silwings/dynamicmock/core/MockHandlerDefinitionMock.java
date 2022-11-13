package top.silwings.dynamicmock.core;

import top.silwings.core.repository.definition.MockHandlerDefinition;
import top.silwings.core.repository.definition.MockResponseDefinition;
import top.silwings.core.repository.definition.MockResponseInfoDefinition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @ClassName MockDefinitionMock
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 23:05
 * @Since
 **/
public class MockHandlerDefinitionMock {

    public static MockHandlerDefinition build() {
        final Random random = new Random();

        final MockHandlerDefinition definition = new MockHandlerDefinition();
        definition.setId(String.valueOf(random.nextInt(1000)));
        definition.setName("MockHandlerDefinitionMock#build");
        definition.setHttpMethods(Arrays.asList("GET"));
        definition.setRequestUri("/user/{name}");
        definition.setLabel("test");
        definition.setDelayTime(0);
        definition.setCustomizeSpace(null);
        definition.setResponses(buildResponseInfo());
        definition.setTasks(null);

        return definition;
    }

    private static List<MockResponseInfoDefinition> buildResponseInfo() {

        final MockResponseInfoDefinition definition = new MockResponseInfoDefinition();
        definition.setName("MockHandlerDefinitionMock#buildResponse");
        definition.setSupport(Collections.emptyList());
        definition.setDelayTime(0);
        definition.setMockResponseDefinition(buildResponse());

        return Collections.singletonList(definition);
    }

    private static MockResponseDefinition buildResponse() {

        final MockResponseDefinition definition = new MockResponseDefinition();
        definition.setStatus(201);
        definition.setHeaders(null);
        definition.setBody("{" +
                "\"${#uuid(def)}\": \"${#uuid(#uuid(3*(1+1)--2-6))}\"," +
                "\"uuidKeyA\": \"${#uuid(1,2)}\"," +
                "\"uuidKeyB\": \"${#uuid()}\"" +
                "}");

        return definition;
    }

}