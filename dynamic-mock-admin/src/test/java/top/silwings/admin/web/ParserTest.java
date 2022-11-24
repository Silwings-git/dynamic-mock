package top.silwings.admin.web;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import top.silwings.admin.DynamicMockAdminApplication;
import top.silwings.admin.web.controller.MockHandlerDefinitionMock;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.JsonNodeParser;
import top.silwings.core.handler.MockHandler;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.handler.MockHandlerPoint;
import top.silwings.core.handler.RequestContext;
import top.silwings.core.handler.tree.Node;
import top.silwings.core.handler.tree.NodeInterpreter;
import top.silwings.core.handler.tree.NodeReader;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.TaskRequestDto;
import top.silwings.core.utils.JsonUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = DynamicMockAdminApplication.class)
@RunWith(SpringRunner.class)
public class ParserTest {

    @Autowired
    private DynamicValueFactory dynamicValueFactory;

    @Autowired
    private JsonNodeParser jsonNodeParser;
    @Autowired
    private MockHandlerManager mockHandlerManager;
    @Autowired
    private MockHandlerFactory mockHandlerFactory;
    @Autowired
    private MockHandlerPoint mockHandlerPoint;

    @Test
    public void test001() {

        final List<String> expressionList = new ArrayList<>();

        expressionList.add("1+2");
        expressionList.add("1+2*3");
        expressionList.add("2+#search(age)");
        expressionList.add("2+#search(age)*2");
        expressionList.add("(2+#search(age))*2+2*4");
        expressionList.add("#uuid(#search(1+1),#search(true))");
        expressionList.add("#uuid(#search(1+1),#search(#search(true)))");
        expressionList.add("1/2");
        expressionList.add("2/1");
        expressionList.add("2-1");
        expressionList.add("1-2");
        expressionList.add("-1");
        expressionList.add("1--1");
        expressionList.add("1--1-1");
        expressionList.add("123,456");
        expressionList.add("#search(#search(#search(#search(param)+#search(param))))");
        expressionList.add("#search(#search(#search(#search(param)+(19-#search(paramA)))))");
        expressionList.add("#search(#search(#search(#search(param)+(20-#search(paramA)--1-2))))");
        expressionList.add("#search(abc.abc)");
        expressionList.add("#isBlank(abc.abc)");
        expressionList.add("#isBlank(abc.abc)");
        expressionList.add("#isBlank()");
        expressionList.add("#isBlank(\"\")");
        expressionList.add("#isBlank() && true");
        expressionList.add("#isBlank() && false");
        expressionList.add("#search(#isBlank())");
        expressionList.add("#search(10+#isBlank())");
        expressionList.add("<2+2>");
        expressionList.add("2+2");
        expressionList.add("<2+2");
        expressionList.add("#eq(#uuid(),1)");
        expressionList.add("#eq(#search(abcMap.list[0],customizeSpace),-1)");
        expressionList.add("#uuid(,,)");
        expressionList.add("#uuid(,,false)");
        expressionList.add("#uuid(,,true)");
        expressionList.add("#uuid(,10)");
        expressionList.add("#uuid(,10,true)");
        // 触发表达式解析异常
//        expressionList.add("#uuid(UserCode-,10,true)");
//        expressionList.add("#uuid(<UserCode->,10,true)");

        expressionList.add("             10  + 1-1        == 11-1 ");
        expressionList.add("#eq(#eq(1,2),false)");
        expressionList.add("#eq(#eq(1,2),true)");
        expressionList.add("#random()");
        expressionList.add("#random(int)");
        expressionList.add("#random(int,1)");
        expressionList.add("#random(int,1,10)");
        expressionList.add("#random(long)");
        expressionList.add("#random(long,1)");
        expressionList.add("#random(long,1,10)");
        expressionList.add("#random(double)");
        expressionList.add("#random(double,1)");
        expressionList.add("#random(double,1.9999999999999999,2.000000000000001)");
        expressionList.add("#random(boolean)");
        expressionList.add("#random(boolean,1)");
        expressionList.add("#random(boolean,1,2)");


        final HashMap<String, Object> abcMap = new HashMap<>();
        abcMap.put("list", Collections.singletonList(-1));
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

        expressionList.forEach(exp -> this.extracted(exp, requestContext));
    }

    private void extracted(final String str, final RequestContext requestContext) {

        final DynamicValue dynamicValue = this.dynamicValueFactory.buildDynamicValue(str);

        final MockHandlerContext mockHandlerContext = MockHandlerContext.builder()
                .requestContext(requestContext)
                .build();

        System.out.println(JsonUtils.toJSONString(new NodeInterpreter(dynamicValue).interpret(mockHandlerContext)));
    }

    @Test
    public void test002() {

        final TestData testData = new TestData();

        final Node analyze1 = this.jsonNodeParser.parse(testData.getTest002());

        final MockHandlerContext mockHandlerContext = MockHandlerContext.builder()
                .requestContext(testData.getRequestContext())
                .build();

        final Object interpret = analyze1.interpret(mockHandlerContext, Collections.emptyList());

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

            final Object interpret = ele.interpret(testData.getMockHandlerContext(), arrayList);

            stack.push(interpret);

        }

        log.info(JsonUtils.toJSONString(stack.pop()));
    }

    @Test
    public void test006() {

        String expression = "1==2";
        expression = "#uuid()";

        final DynamicValue dynamicValue = this.dynamicValueFactory.buildDynamicValue(expression);

        final NodeInterpreter nodeInterpreter = new NodeInterpreter(dynamicValue);

        final Object interpret = nodeInterpreter.interpret(MockHandlerContext.builder().build());

        System.out.println("interpret = " + interpret);

    }

    @Test
    public void test007() throws InterruptedException {

        final MockHandlerDto definition = MockHandlerDefinitionMock.build();
        final MockHandler mockHandler = this.mockHandlerFactory.buildMockHandler(definition);
        this.mockHandlerManager.registerHandler(mockHandler);

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(definition.getHttpMethods().get(1).name());
//        request.addHeader("Content-Type","application/json");
        request.setRequestURI(definition.getRequestUri().replace("{", "").replace("}", ""));
        request.setContent("{\"pageNum\": \"11\",\"pageSize\": \"10\"}".getBytes(StandardCharsets.UTF_8));

        final ResponseEntity<Object> responseEntity = this.mockHandlerPoint.executeMock(request);

        log.info(JsonUtils.toJSONString(responseEntity.getBody()));

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void test003() {

        TaskRequestDto httpTaskRequestInfoDefinition = JsonUtils.toBean("{\n" +
                "  \"body\": {\n" +
                "    \"name\": [\"御坂美琴\",\"白井黑子\"]\n" +
                "  }\n" +
                "}", TaskRequestDto.class);

        System.out.println(JsonUtils.toJSONString(httpTaskRequestInfoDefinition));

        httpTaskRequestInfoDefinition = JsonUtils.toBean("{\n" +
                "  \"abc\": {\n" +
                "    \"name\": [\"御坂美琴\",\"白井黑子\"]\n" +
                "  }\n" +
                "}", TaskRequestDto.class);

        System.out.println(JsonUtils.toJSONString(httpTaskRequestInfoDefinition));

    }

    @Getter
    public static class TestData {
        final MockHandlerContext mockHandlerContext;
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

        public TestData() {
            this.requestContext.addCustomizeParam("param", 1);
            this.requestContext.addCustomizeParam("a2", "name");
            this.requestContext.addCustomizeParam("name", "御坂美琴");
            this.requestContext.addCustomizeParam("age", 14);
            this.requestContext.addCustomizeParam("abcabc", "A御坂美琴A");
            this.requestContext.addCustomizeParam("def", "B御坂美琴B");
            mockHandlerContext = MockHandlerContext.builder()
                    .requestContext(this.requestContext)
                    .build();
        }
    }

}
