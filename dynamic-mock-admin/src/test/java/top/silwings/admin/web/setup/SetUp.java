package top.silwings.admin.web.setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.silwings.admin.web.controller.ProjectController;
import top.silwings.admin.web.vo.param.MockHandlerInfoParam;
import top.silwings.admin.web.vo.param.MockResponseInfoParam;
import top.silwings.admin.web.vo.param.MockResponseParam;
import top.silwings.admin.web.vo.param.SaveProjectParam;
import top.silwings.admin.web.vo.param.SaveTaskInfoParam;
import top.silwings.admin.web.vo.param.SaveTaskRequestInfoParam;
import top.silwings.core.common.Identity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName MockHandlerSetUp
 * @Description
 * @Author Silwings
 * @Date 2022/11/16 22:51
 * @Since
 **/
@Component
public class SetUp {

    @Autowired
    private ProjectController projectController;

    public static MockHandlerInfoParam buildHandler(final Identity project) {
        return MockHandlerInfoParam.builder()
                .projectId(project)
                .name("TEST_MOCK_HANDLER")
                .httpMethods(Stream.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE).map(HttpMethod::name).collect(Collectors.toList()))
                .requestUri("/" + ThreadLocalRandom.current().nextInt(10000))
                .label("TEST_MOCK_HANDLER")
                .delayTime(0)
                .customizeSpace(buildCustomizeSpace())
                .responses(buildMockResponseInfoVoList())
                .tasks(buildTaskList())
                .build();
    }

    public static MockHandlerInfoParam update(final Identity handlerId) {
        return MockHandlerInfoParam.builder()
                .handlerId(handlerId)
                .name("TEST_MOCK_HANDLER")
                .httpMethods(Stream.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE).map(HttpMethod::name).collect(Collectors.toList()))
                .requestUri("/" + ThreadLocalRandom.current().nextInt(10000))
                .label("TEST_MOCK_HANDLER")
                .delayTime(0)
                .customizeSpace(buildCustomizeSpace())
                .responses(buildMockResponseInfoVoList())
                .tasks(buildTaskList())
                .build();
    }

    private static List<SaveTaskInfoParam> buildTaskList() {

        final Map<String, String> body = new HashMap<>();
        body.put("name", "${#search('name','customizeSpace')}");
        body.put("underAge", "${#search('age','customizeSpace')<18}");

        final SaveTaskRequestInfoParam requestInfoVo = SaveTaskRequestInfoParam.builder()
                .requestUrl("localhost:8888/test")
                .httpMethod(HttpMethod.GET.name())
                .headers(buildHeaders())
                .body(body)
                .uriVariables(new HashMap<>())
                .build();

        final SaveTaskInfoParam mockTask = SaveTaskInfoParam.builder()
                .name("TEST_MOCK_TASK")
                .support(Collections.singletonList("${#search('age','customizeSpace')==14&&#eq(#search('parameterMap.execute[0]','requestInfo'),1)}"))
                .async(true)
                .cron("* * * * * ?")
                .numberOfExecute(3)
                .request(requestInfoVo)
                .build();

        return Collections.singletonList(mockTask);
    }

    private static List<MockResponseInfoParam> buildMockResponseInfoVoList() {

        final Map<String, Object> body = new HashMap<>();
        body.put("name", "${#search('name','customizeSpace')}");
        body.put("age", "${#search('age','customizeSpace')}");
        body.put("level", "${#search('level','customizeSpace')}");

        final MockResponseParam responseVo = MockResponseParam.builder()
                .status(200)
                .headers(buildHeaders())
                .body(body)
                .build();

        final MockResponseInfoParam mockResponse = MockResponseInfoParam.builder()
                .name("TEST_MOCK_RESPONSE")
                .support(Collections.singletonList("1+1==2"))
                .delayTime(10)
                .response(responseVo)
                .build();

        return Collections.singletonList(mockResponse);
    }

    private static Map<String, List<String>> buildHeaders() {
        final HashMap<String, List<String>> headerMap = new HashMap<>();

        headerMap.put("content-type", Collections.singletonList("application/json"));
        headerMap.put("aaaaa", Collections.singletonList("localhost:8080/ybmq"));
        headerMap.put("accept-encoding", Collections.singletonList("gzip, deflate, br"));
        headerMap.put("connection", Collections.singletonList("keep-alive"));

        return headerMap;
    }

    private static Map<String, Object> buildCustomizeSpace() {

        final Map<String, Object> customizeSpaceMap = new HashMap<>();
        customizeSpaceMap.put("name", "Misaka Mikoto");
        customizeSpaceMap.put("age", 14);
        customizeSpaceMap.put("level", 5);

        return customizeSpaceMap;
    }

    public Identity createProject() {
        final SaveProjectParam saveProjectParam = new SaveProjectParam();
        saveProjectParam.setProjectName("JUNIT_TEST");

        return this.projectController.save(saveProjectParam).getData();
    }

}