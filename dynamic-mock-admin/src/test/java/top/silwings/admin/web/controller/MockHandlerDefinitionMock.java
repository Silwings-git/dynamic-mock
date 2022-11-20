package top.silwings.admin.web.controller;

import org.springframework.http.HttpMethod;
import top.silwings.core.common.Identity;
import top.silwings.core.model.dto.MockHandlerDto;
import top.silwings.core.model.dto.MockResponseDto;
import top.silwings.core.model.dto.MockResponseInfoDto;
import top.silwings.core.model.dto.TaskInfoDto;
import top.silwings.core.model.dto.TaskRequestDto;
import top.silwings.core.utils.JsonUtils;

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

    public static MockHandlerDto build() {
        final Random random = new Random();
        final MockHandlerDto definition = MockHandlerDto.builder()
                .name("MockHandlerDefinitionMock#build")
                .httpMethods(Arrays.asList(HttpMethod.GET, HttpMethod.POST))
                .requestUri("/user/{name}")
                .label("test")
                .delayTime(0)
                .customizeSpace(buildCustomizeSpace())
                .responses(buildResponseInfo())
                .tasks(buildTasksInfo())
                .handlerId(Identity.from(random.nextLong()))
                .build();
        return definition;
    }

    private static Map<String, Object> buildCustomizeSpace() {

        final Map<String, Object> map = new HashMap<>();
        map.put("name", "御坂美琴");
        map.put("phoneNumbers", Arrays.asList("180", "188"));
        map.put("11", "Random");

        return map;
    }

    private static List<TaskInfoDto> buildTasksInfo() {

        final TaskInfoDto.TaskInfoDtoBuilder builderA = TaskInfoDto.builder();
        builderA.name("MockHandlerDefinitionMock#build");
        builderA.support(Collections.emptyList());
        builderA.async(true);
        builderA.cron(null);
        builderA.numberOfExecute(3);
        builderA.request(buildMockTask());
        final TaskInfoDto def1 = builderA.build();

        final TaskInfoDto.TaskInfoDtoBuilder def2 = TaskInfoDto.builder();
        def2.name("MockHandlerDefinitionMock#build#同步任务");
        def2.support(Collections.emptyList());
        def2.async(false);
        def2.cron(null);
        def2.numberOfExecute(1);
        def2.request(buildMockTask2());

        return Arrays.asList(def1, def2.build());
    }

    private static TaskRequestDto buildMockTask2() {
        final TaskRequestDto.TaskRequestDtoBuilder definition = TaskRequestDto.builder();

        final HashMap<String, List<String>> uriVariables = new HashMap<>();
        uriVariables.put("name", Arrays.asList("${#uuid()}"));
        uriVariables.put("age", Arrays.asList("10", "20"));

        definition.requestUrl("http://localhost:8080/silwings");
        definition.httpMethod(HttpMethod.GET);
        definition.uriVariables(uriVariables);

        return definition.build();
    }

    private static TaskRequestDto buildMockTask() {

        final TaskRequestDto.TaskRequestDtoBuilder definition = TaskRequestDto.builder();

        final HashMap<String, List<String>> headers = new HashMap<>();
        headers.put("userAuthToken", Arrays.asList("${#uuid()}"));
        headers.put("dp", Arrays.asList("1", "${#uuid()}"));


        definition.requestUrl("http://localhost:8080/404");
        definition.httpMethod(HttpMethod.GET);
        definition.headers(headers);

        return definition.build();
    }

    private static List<MockResponseInfoDto> buildResponseInfo() {

        final MockResponseInfoDto.MockResponseInfoDtoBuilder definition = MockResponseInfoDto.builder();
        definition.name("MockHandlerDefinitionMock#buildResponse");
        definition.support(Collections.emptyList());
        definition.delayTime(0);
        definition.response(buildResponse());

        return Collections.singletonList(definition.build());
    }

    private static MockResponseDto buildResponse() {
        final HashMap<String, String> body = new HashMap<>();

        final MockResponseDto.MockResponseDtoBuilder definition = MockResponseDto.builder();
        definition.status(201);
        definition.headers(null);

        final String s = "{" +
                "\"${#uuid()}\": \"${#uuid()}\"," +
                "\"uuidKeyA\": \"${#uuid()}\"," +
                "\"uuidKeyB\": \"${#uuid()}\"," +
                "\"name\": \"${#search(name)}\"," +
                "\"phoneNumber\": \"${#search(<$.phoneNumbers[0]>)}\"," +
                "\"uri\": \"${#search(<$.requestURI>,requestInfo)}\"," +
                "\"random\": \"${#search($.+(1+2*5))}\"," +
                " \"body\": \"${#pageData(#search(<$.body.pageNum>,requestInfo),#search(<$.body.pageSize>,requestInfo),101,{\\\"code\\\": \\\"${#search(name)}\\\",\\\"status\\\": \\\"${#uuid()}\\\"})}\"" +
                "}";
        definition.body(JsonUtils.toBean(s));


        return definition.build();
    }

}