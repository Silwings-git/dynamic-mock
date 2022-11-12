package top.silwings.dynamicmock.core;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.silwings.core.MockSpringApplication;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.JsonNodeParser;
import top.silwings.core.handler.ParameterContext;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.DynamicValueFactory;
import top.silwings.core.handler.node.Node;
import top.silwings.core.repository.definition.RequestDefinition;
import top.silwings.core.utils.NodeTraversalUtils;

import java.util.ArrayList;
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

        final DynamicValue dynamicValue = this.dynamicValueFactory.buildDynamicValue(str);

        final ParameterContext parameterContext = new ParameterContext();
        parameterContext.putParameter("paramA", -1);
        parameterContext.putParameter("param", 20);
        parameterContext.putParameter("40", true);
        parameterContext.putParameter("age", 18);
        parameterContext.putParameter("2", 15);
        parameterContext.putParameter("true", 10);
        parameterContext.putParameter("10", "御坂美琴");
        parameterContext.putParameter("10true", "御坂美琴");

        final Context context = Context.builder()
                .parameterContext(parameterContext)
                .build();

        System.out.println(JSON.toJSONString(dynamicValue.interpret(context)));
    }

    @Test
    public void test002() {

        final TestData testData = new TestData();

        final Node analyze1 = this.jsonNodeParser.parse(testData.getTest002());

        final Context context = Context.builder()
                .parameterContext(testData.getParameterContext())
                .builder(new StringBuilder(testData.getTest002().length()))
                .build();

        final Object interpret = analyze1.interpret(context);

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

            final List<Object> arrayList = new ArrayList<>();
            if (nodeCount > 0) {
                for (int i = 0; i < nodeCount; i++) {
                    arrayList.add(stack.pop());
                }
            }

            final Object interpret = ele.interpret(testData.getContext(), arrayList);

            stack.push(interpret);

        }

        log.info(JSON.toJSONString(stack.pop()));
    }

    @Getter
    public static class TestData {
        private final String test001 = "#search(#search(#search(#search(param)+(20-#search(paramA)--1-2))))";
        private final String test002 = "{\n" +
                "\"id\":\"${#uuid(abc)}\"," +
                "\"${#search(def)}\": \"${#search(#search(3*(1+1)--2-6))}\"," +
                "\"${#search(abc.abc)}\": \"${#search(#search(3*(1+1)--2-6))}\"," +
                "\"age\": \"${#search(age)}\"," +
                "\"happy\": \"${             10  + 1-1        == 11-1 }\"" +
                "}";
        private final ParameterContext parameterContext = new ParameterContext();

        final Context context;

        public TestData() {
            this.parameterContext.putParameter("param", 1);
            this.parameterContext.putParameter("2", "name");
            this.parameterContext.putParameter("name", "御坂美琴");
            this.parameterContext.putParameter("age", 14);
            this.parameterContext.putParameter("abc.abc", "A御坂美琴A");
            this.parameterContext.putParameter("def", "B御坂美琴B");
            context = Context.builder()
                    .parameterContext(this.parameterContext)
                    .builder(new StringBuilder())
                    .build();
        }
    }

    @Test
    public void test003() {

        final RequestDefinition httpTaskRequestInfoDefinition = JSON.parseObject("{\n" +
                "  \"map\": {\n" +
                "    \"name\": [\"御坂美琴\",\"白井黑子\"]\n" +
                "  }\n" +
                "}", RequestDefinition.class);

        System.out.println(httpTaskRequestInfoDefinition);

    }

}
