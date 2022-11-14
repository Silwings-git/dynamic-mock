package top.silwings.dynamicmock.core;

import top.silwings.core.repository.definition.MockHandlerDefinition;
import top.silwings.core.repository.definition.MockResponseDefinition;
import top.silwings.core.repository.definition.MockResponseInfoDefinition;
import top.silwings.core.repository.definition.MockTaskDefinition;
import top.silwings.core.repository.definition.MockTaskInfoDefinition;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        definition.setHttpMethods(Arrays.asList("GET", "POST"));
        definition.setRequestUri("/user/{name}");
        definition.setLabel("test");
        definition.setDelayTime(0);
        definition.setCustomizeSpace(buildCustomizeSpace());
        definition.setResponses(buildResponseInfo());
        definition.setTasks(buildTasksInfo());

        return definition;
    }

    private static Map<String, Object> buildCustomizeSpace() {

        final Map<String, Object> map = new HashMap<>();
        map.put("name", "御坂美琴");
        map.put("phoneNumbers", Arrays.asList("180", "188"));
        map.put("11", "Random");

        return map;
    }

    private static List<MockTaskInfoDefinition> buildTasksInfo() {

        final MockTaskInfoDefinition def1 = new MockTaskInfoDefinition();
        def1.setName("MockHandlerDefinitionMock#build");
        def1.setSupport(Collections.emptyList());
        def1.setAsync(true);
        def1.setCron(null);
        def1.setNumberOfExecute(3);
        def1.setMockTaskDefinition(buildMockTask());

        final MockTaskInfoDefinition def2 = new MockTaskInfoDefinition();
        def2.setName("MockHandlerDefinitionMock#build#同步任务");
        def2.setSupport(Collections.emptyList());
        def2.setAsync(false);
        def2.setCron(null);
        def2.setNumberOfExecute(1);
        def2.setMockTaskDefinition(buildMockTask2());


        return Arrays.asList(def1, def2);
    }

    private static MockTaskDefinition buildMockTask2() {
        final MockTaskDefinition definition = new MockTaskDefinition();

        final HashMap<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("name", "${#uuid()}");
        uriVariables.put("age", Arrays.asList("10", "20"));

        definition.setRequestUrl("http://localhost:8080/silwings");
        definition.setHttpMethod("GET");
        definition.setUriVariables(uriVariables);

        return definition;
    }

    private static MockTaskDefinition buildMockTask() {

        final MockTaskDefinition definition = new MockTaskDefinition();

        final HashMap<String, Object> headers = new HashMap<>();
        headers.put("userAuthToken", "${#uuid()}");
        headers.put("dp", Arrays.asList("1", "${#uuid()}"));


        definition.setRequestUrl("http://localhost:8080/404");
        definition.setHttpMethod("GET");
        definition.setHeaders(headers);

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
        final HashMap<String, String> body = new HashMap<>();

        final MockResponseDefinition definition = new MockResponseDefinition();
        definition.setStatus(201);
        definition.setHeaders(null);

        final String s = "{" +
//                "\"${#uuid()}\": \"${#uuid()}\"," +
//                "\"uuidKeyA\": \"${#uuid()}\"," +
//                "\"uuidKeyB\": \"${#uuid()}\"," +
//                "\"name\": \"${#search(name)}\"," +
//                "\"phoneNumber\": \"${#search(<$.phoneNumbers[0]>)}\"," +
//                "\"uri\": \"${#search(<$.requestURI>,requestInfo)}\"," +
//                "\"random\": \"${#search($.+(1+2*5))}\"," +
                " \"body\": \"${#pageData(#search(<$.body.pageNum>,requestInfo),#search(<$.body.pageSize>,requestInfo),101,{\\\"code\\\": \\\"${#search(name)}\\\",\\\"status\\\": \\\"${#uuid()}\\\"})}\"" +
                "}";
        definition.setBody(s);


        return definition;
    }

}