package top.silwings.dynamicmock.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.silwings.core.MockSpringApplication;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.JsonNodeParser;
import top.silwings.core.handler.MockHandler;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.handler.RequestContext;
import top.silwings.core.handler.task.MockTaskManager;
import top.silwings.core.handler.tree.Node;
import top.silwings.core.handler.tree.NodeInterpreter;
import top.silwings.core.handler.tree.NodeReader;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.repository.dto.MockHandlerDto;
import top.silwings.core.repository.dto.TaskRequestDto;
import top.silwings.core.utils.JsonUtils;
import top.silwings.core.web.MockHandlerPoint;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = MockSpringApplication.class)
@RunWith(SpringRunner.class)
public class ParserTest {

    private final NoHandlerFoundException noHandlerFoundException = new NoHandlerFoundException("GET", "", new HttpHeaders());

    @Autowired
    private DynamicValueFactory dynamicValueFactory;

    @Autowired
    private JsonNodeParser jsonNodeParser;

    @Test
    public void test001() {

        String str;
        str = "1+2";
        str = "1+2*3";
        str = "2+#search(age)";
        str = "2+#search(age)*2";
        str = "(2+#search(age))*2+2*4";
        str = "#uuid(#search(1+1),#search(true))";
        str = "#uuid(#search(1+1),#search(#search(true)))";
        str = "1/2";
        str = "2/1";
        str = "2-1";
        str = "1-2";
        str = "-1";
        str = "1--1";
        str = "1--1-1";
        str = "123,456";
        str = "#search(#search(#search(#search(param)+#search(param))))";
        str = "#search(#search(#search(#search(param)+(19-#search(paramA)))))";
        str = "#search(#search(#search(#search(param)+(20-#search(paramA)--1-2))))";
        str = "#search(abc.abc)";
        str = "#isBlank(abc.abc)";
        str = "#isBlank(abc.abc)";
        str = "#isBlank()";
        str = "#isBlank(\"\")";
        str = "#isBlank() && true";
        str = "#isBlank() && false";
        str = "#search(#isBlank())";
        str = "#search(10+#isBlank())";
        str = "<2+2>";
        str = "2+2";
        str = "<2+2";
        str = "#eq(#uuid(),1)";
        str = "#eq(#search(abcMap.list[0],customizeSpace),-1)";
        str = "#uuid(,,)";
        str = "#uuid(,,false)";
        str = "#uuid(,,true)";
        str = "#uuid(,10)";
        str = "#uuid(,10,true)";
        // 触发表达式解析异常
        str = "#uuid(UserCode-,10,true)";
        str = "#uuid(<UserCode->,10,true)";
        str = "             10  + 1-1        == 11-1 ";


        final DynamicValue dynamicValue = this.dynamicValueFactory.buildDynamicValue(str);

        final HashMap<String, Object> abcMap = new HashMap<>();
        abcMap.put("list", Arrays.asList(-1));

        final RequestContext requestContext = RequestContext.builder().customizeSpace(new HashMap<>()).build();
        requestContext.addCustomizeParam("paramA", -1);
        requestContext.addCustomizeParam("abcMap", abcMap);
        requestContext.addCustomizeParam("param", 20);
        requestContext.addCustomizeParam("40", true);
        requestContext.addCustomizeParam("age", 18);
        requestContext.addCustomizeParam("2", 15);
        requestContext.addCustomizeParam("true", 10);
        requestContext.addCustomizeParam("10", "御坂美琴");
        requestContext.addCustomizeParam("10true", "御坂美琴");

        final Context context = Context.builder()
                .requestContext(requestContext)
                .build();

        System.out.println(JsonUtils.toJSONString(new NodeInterpreter(dynamicValue).interpret(context)));
    }

    @Test
    public void test002() {

        final TestData testData = new TestData();

        final Node analyze1 = this.jsonNodeParser.parse(testData.getTest002());

        final Context context = Context.builder()
                .requestContext(testData.getRequestContext())
                .build();

        final Object interpret = analyze1.interpret(context, Collections.emptyList());

        log.info(JsonUtils.toJSONString(interpret));
    }

    @Test
    public void test004() {

        final TestData testData = new TestData();

        final Node node = this.jsonNodeParser.parse(testData.getTest002());

        final List<Node> nodeList = NodeReader.postOrderTraversal(node);

        final Stack<Object> stack = new Stack<>();

        for (final Node ele : nodeList) {

            final int nodeCount = ele.getNodeCount();

            if (stack.size() < nodeCount) {
                throw new DynamicMockException("缺少参数");
            }

            final List<Object> arrayList = new ArrayList<>();
            if (nodeCount > 0) {
                for (int i = 0; i < nodeCount; i++) {
                    arrayList.add(stack.pop());
                }
            }
            Collections.reverse(arrayList);

            final Object interpret = ele.interpret(testData.getContext(), arrayList);

            stack.push(interpret);

        }

        log.info(JsonUtils.toJSONString(stack.pop(), SerializerFeature.WriteMapNullValue));
    }

    @Test
    public void test006() {

        String expression = "1==2";
        expression = "#uuid()";

        final DynamicValue dynamicValue = this.dynamicValueFactory.buildDynamicValue(expression);

        final NodeInterpreter nodeInterpreter = new NodeInterpreter(dynamicValue);

        final Object interpret = nodeInterpreter.interpret(Context.builder().build());

        System.out.println("interpret = " + interpret);

    }

    @Autowired
    private MockHandlerManager mockHandlerManager;

    @Autowired
    private MockHandlerFactory mockHandlerFactory;

    @Autowired
    private MockHandlerPoint mockHandlerPoint;

    @Autowired
    private MockTaskManager mockTaskManager;

    @Test
    public void test007() throws InterruptedException, NoHandlerFoundException {

        final MockHandlerDto definition = MockHandlerDefinitionMock.build();
        final MockHandler mockHandler = this.mockHandlerFactory.buildMockHandler(definition);
        this.mockHandlerManager.registerHandler(mockHandler);

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(definition.getHttpMethods().get(1).name());
//        request.addHeader("Content-Type","application/json");
        request.setRequestURI(definition.getRequestUri().replace("{", "").replace("}", ""));
        request.setContent("{\"pageNum\": \"11\",\"pageSize\": \"10\"}".getBytes(StandardCharsets.UTF_8));

        final ResponseEntity<Object> responseEntity = this.mockHandlerPoint.executeMock(this.noHandlerFoundException, request);

        log.info(JsonUtils.toJSONString(responseEntity.getBody(), SerializerFeature.WriteMapNullValue));

        TimeUnit.SECONDS.sleep(5);
    }

    @Getter
    public static class TestData {
        private final String test001 = "#search(#search(#search(#search(param)+(20-#search(paramA)--1-2))))";
        private final String test002 = "{\n" +
                "\"id\":\"${#uuid(abc)}\"," +
                "\"${#search(def)}\": \"${#search(#search(a+(3*(1+1)--2-6)))}\"," +
                "\"${#search(<abcabc>)}\": \"${#search(#search(a+(3*(1+1)--2-6)))}\"," +
                "\"age\": \"${#search(age)}\"," +
                "\"zbd\": \"${#search(zbd)}\"," +
                "\"happy\": \"${             10  + 1-1        == 11-1 }\"," +
                "\"uuidKey\": \"${#uuid(1,2)}\"" +
                "}";

        private final String test004 = "{\n" +
                "\"${#search(def)}\": \"${#search(#search(3*(1+1)--2-6))}\"," +
                "\"uuidKey\": \"${#uuid(1,2)}\"" +
                "}";
        private final RequestContext requestContext = RequestContext.builder().customizeSpace(new HashMap<>()).build();

        final Context context;

        public TestData() {
            this.requestContext.addCustomizeParam("param", 1);
            this.requestContext.addCustomizeParam("a2", "name");
            this.requestContext.addCustomizeParam("name", "御坂美琴");
            this.requestContext.addCustomizeParam("age", 14);
            this.requestContext.addCustomizeParam("abcabc", "A御坂美琴A");
            this.requestContext.addCustomizeParam("def", "B御坂美琴B");
            context = Context.builder()
                    .requestContext(this.requestContext)
                    .build();
        }
    }

    @Test
    public void test003() {

        final TaskRequestDto httpTaskRequestInfoDefinition = JSON.parseObject("{\n" +
                "  \"map\": {\n" +
                "    \"name\": [\"御坂美琴\",\"白井黑子\"]\n" +
                "  }\n" +
                "}", TaskRequestDto.class);

        System.out.println(httpTaskRequestInfoDefinition);

    }

}
