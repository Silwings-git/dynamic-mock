package top.silwings.dynamicmock.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import top.silwings.core.MockSpringApplication;
import top.silwings.core.exceptions.DynamicDataException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.HandlerContext;
import top.silwings.core.handler.JsonNodeParser;
import top.silwings.core.handler.MockHandler;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.handler.task.MockTaskManager;
import top.silwings.core.handler.tree.Node;
import top.silwings.core.handler.tree.NodeInterpreter;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.repository.definition.MockHandlerDefinition;
import top.silwings.core.repository.definition.MockTaskDefinition;
import top.silwings.core.utils.NodeTraversalUtils;
import top.silwings.core.web.MockHandlerPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

@Slf4j
@SpringBootTest(classes = MockSpringApplication.class)
@RunWith(SpringRunner.class)
public class ParserTest {

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

        final DynamicValue dynamicValue = this.dynamicValueFactory.buildDynamicValue(str);

        final HandlerContext handlerContext = HandlerContext.builder().customizeSpace(new HashMap<>()).build();
        handlerContext.addCustomizeParam("paramA", -1);
        handlerContext.addCustomizeParam("param", 20);
        handlerContext.addCustomizeParam("40", true);
        handlerContext.addCustomizeParam("age", 18);
        handlerContext.addCustomizeParam("2", 15);
        handlerContext.addCustomizeParam("true", 10);
        handlerContext.addCustomizeParam("10", "御坂美琴");
        handlerContext.addCustomizeParam("10true", "御坂美琴");

        final Context context = Context.builder()
                .handlerContext(handlerContext)
                .build();

        System.out.println(JSON.toJSONString(new NodeInterpreter(dynamicValue).interpret(context)));
    }

    @Test
    public void test002() {

        final TestData testData = new TestData();

        final Node analyze1 = this.jsonNodeParser.parse(testData.getTest002());

        final Context context = Context.builder()
                .handlerContext(testData.getHandlerContext())
                .build();

        final Object interpret = analyze1.interpret(context, Collections.emptyList());

        log.info(JSON.toJSONString(interpret));
    }

    @Test
    public void test004() {

        final TestData testData = new TestData();

        final Node node = this.jsonNodeParser.parse(testData.getTest002());

        final List<Node> nodeList = NodeTraversalUtils.postOrderTraversal(node);

        final Stack<Object> stack = new Stack<>();

        for (final Node ele : nodeList) {

            final int nodeCount = ele.getNodeCount();

            if (stack.size() < nodeCount) {
                throw new DynamicDataException("缺少参数");
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

        log.info(JSON.toJSONString(stack.pop(), SerializerFeature.WriteMapNullValue));
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
    public void test007() throws InterruptedException {

        final MockHandlerDefinition definition = MockHandlerDefinitionMock.build();
        final MockHandler mockHandler = this.mockHandlerFactory.buildMockHandler(definition);
        this.mockHandlerManager.registerHandler(mockHandler);

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(definition.getHttpMethods().get(0));
        request.setRequestURI(definition.getRequestUri().replace("{", "").replace("}", ""));

        final ResponseEntity<Object> responseEntity = this.mockHandlerPoint.executeMock(request);

        log.info(JSON.toJSONString(responseEntity.getBody(), SerializerFeature.WriteMapNullValue));
    }

    @Getter
    public static class TestData {
        private final String test001 = "#search(#search(#search(#search(param)+(20-#search(paramA)--1-2))))";
        private final String test002 = "{\n" +
                "\"id\":\"${#uuid(abc)}\"," +
                "\"${#search(def)}\": \"${#search(#search(3*(1+1)--2-6))}\"," +
                "\"${#search(abc.abc)}\": \"${#search(#search(3*(1+1)--2-6))}\"," +
                "\"age\": \"${#search(age)}\"," +
                "\"zbd\": \"${#search(zbd)}\"," +
                "\"happy\": \"${             10  + 1-1        == 11-1 }\"," +
                "\"uuidKey\": \"${#uuid(1,2)}\"" +
                "}";

        private final String test004 = "{\n" +
                "\"${#search(def)}\": \"${#search(#search(3*(1+1)--2-6))}\"," +
                "\"uuidKey\": \"${#uuid(1,2)}\"" +
                "}";
        private final HandlerContext handlerContext = HandlerContext.builder().customizeSpace(new HashMap<>()).build();

        final Context context;

        public TestData() {
            this.handlerContext.addCustomizeParam("param", 1);
            this.handlerContext.addCustomizeParam("2", "name");
            this.handlerContext.addCustomizeParam("name", "御坂美琴");
            this.handlerContext.addCustomizeParam("age", 14);
            this.handlerContext.addCustomizeParam("abc.abc", "A御坂美琴A");
            this.handlerContext.addCustomizeParam("def", "B御坂美琴B");
            context = Context.builder()
                    .handlerContext(this.handlerContext)
                    .build();
        }
    }

    @Test
    public void test003() {

        final MockTaskDefinition httpTaskRequestInfoDefinition = JSON.parseObject("{\n" +
                "  \"map\": {\n" +
                "    \"name\": [\"御坂美琴\",\"白井黑子\"]\n" +
                "  }\n" +
                "}", MockTaskDefinition.class);

        System.out.println(httpTaskRequestInfoDefinition);

    }

}
