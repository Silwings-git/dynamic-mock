package top.silwings.admin.web.setup;

import org.springframework.http.HttpMethod;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.MockResponseDto;
import top.silwings.core.model.MockResponseInfoDto;
import top.silwings.core.model.TaskInfoDto;
import top.silwings.core.model.TaskRequestDto;
import top.silwings.core.utils.JsonUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return MockHandlerDto.builder()
                .name("MockHandlerDefinitionMock#build")
                .httpMethods(Arrays.asList(HttpMethod.GET, HttpMethod.POST))
                .requestUri("/jmh")
                .label("test")
                .delayTime(0)
                .customizeSpace(buildCustomizeSpace())
                .responses(buildResponseInfo())
                .tasks(buildTasksInfo())
                .updateTime(new Date())
                .handlerId(Identity.from(random.nextInt(1000)))
                .build();
    }

    private static Map<String, Object> buildCustomizeSpace() {

        final Map<String, Object> map = new HashMap<>();
        map.put("name", "御坂美琴");
        map.put("phoneNumbers", Arrays.asList("180", "188"));
        map.put("11", "Random");
        map.put("pageNum", 1);
        map.put("pageSize", 10);
        map.put("list6", Stream.iterate(0, i -> i + 2).limit(200).collect(Collectors.toList()));

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

        final TaskInfoDto.TaskInfoDtoBuilder builderB = TaskInfoDto.builder();
        builderB.name("MockHandlerDefinitionMock#build#同步任务");
        builderB.support(Collections.emptyList());
        builderB.async(false);
        builderB.cron(null);
        builderB.numberOfExecute(1);
        builderB.request(buildMockTask2());
        final TaskInfoDto def2 = builderB.build();

        return Collections.emptyList();
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

        final MockResponseDto.MockResponseDtoBuilder definition = MockResponseDto.builder();
        definition.status(201);
        definition.headers(null);

        final String s = "{" +
                "\"${#uuid()}\": \"${#uuid()}\"," +
                "\"uuidKeyA\": \"${#uuid()}\"," +
                "\"uuidKeyB\": \"${#uuid()}\"," +
                "\"name\": \"${#search('name')}\"," +
                "\"phoneNumber\": \"${#search('$.phoneNumbers[0]')}\"," +
                "\"uri\": \"${#search('$.requestURI','requestInfo')}\"," +
                "\"random\": \"${#search('$.'+(1+2*5))}\"," +
                " \"body\": \"${#page(#search('$.body.pageNum','requestInfo'),#search('$.body.pageSize','requestInfo'),101,'{\\\"code\\\": \\\"${#search(^'name^')}\\\",\\\"status\\\": \\\"${#uuid()}\\\"}')}\"" +
                "}";

        final String dynamicPage = "{" +
                " \"body\": \"${#page(#search('$.pageNum'),#search('$.pageSize'),101,'{\\\"code\\\": \\\"CD001\\\",\\\"status\\\": \\\"2\\\"}')}\"" +
                "}";

        final String staticPage = "{" +
                " \"body\": \"${#page(#search('$.pageNum'),#search('$.pageSize'),101,'{\\\"code\\\": \\\"CD001\\\",\\\"status\\\": \\\"2\\\"}',false)}\"" +
                "}";

        final String search = "{" +
                " \"body\": \"${#search('$.pageNum')}\"" +
                "}";

        final String iterator = "{" +
                " \"list\": \"${#page(1,10,100,'${#search(^'$.list6[^'+#search(#saveCache(^'index^',#search(^'index^',^'localCache^',-1)+1,^'key^'),^'localCache^')+^']^',^'customizeSpace^')}')}\"" +
                "}";

        final String iteratorObj = "{" +
                " \"list\": \"${#page(1,10,100,'{\\\"code\\\": \\\"CD001\\\",\\\"status\\\": \\\"${#search(^'$.list6[^'+#search(#saveCache(^'index^',#search(^'index^',^'localCache^',-1)+1,^'key^'),^'localCache^')+^']^',^'customizeSpace^')}\\\"}')}\"" +
                "}";

        definition.body(JsonUtils.toBean(iteratorObj));

        return definition.build();
    }

}