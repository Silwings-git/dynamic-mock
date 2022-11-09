package top.silwings.dynamicmock.core;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.JsonNodeParser;
import top.silwings.core.handler.ParameterContext;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.DynamicValueFactory;
import top.silwings.core.handler.node.Node;

@Slf4j
public class ParserTest {

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

        final DynamicValue dynamicValue = new DynamicValueFactory().buildDynamicValue(str);

        final ParameterContext context = new ParameterContext();
        context.putParameter("paramA", -1);
        context.putParameter("param", 20);
        context.putParameter("40", true);
        context.putParameter("age", 18);
        context.putParameter("2", 15);
        context.putParameter("true", 10);
        context.putParameter("10", "御坂美琴");

        System.out.println(JSON.toJSONString(dynamicValue.value(context)));
    }

    @Test
    public void test002() {

        final TestData testData = new TestData();

        final Node analyze1 = testData.getJsonNodeParser().parse(testData.getTest002());

        final Context context = new Context(testData.getTest002().length(), testData.getParameterContext());

        analyze1.interpret(context);

        log.info(context.getJsonStr());
    }

    @Getter
    public static class TestData {
        private final String test001 = "#search(#search(#search(#search(param)+(20-#search(paramA)--1-2))))";
        private final String test002 =  "{\n" +
                "\"id\":\"${#uuid(abc)}\"," +
                "\"name\": \"${#search(#search(3*(1+1)--2-6))}\"," +
                "\"age\": \"${#search(age)}\"," +
                "\"happy\": \"${             10  + 1-1        == 11-1 }\"" +
                "}";
        private final JsonNodeParser jsonNodeParser = new JsonNodeParser(new DynamicValueFactory());
        private final ParameterContext parameterContext = new ParameterContext();

        public TestData() {
            this.parameterContext.putParameter("param", 1);
            this.parameterContext.putParameter("2", "name");
            this.parameterContext.putParameter("name", "御坂美琴");
            this.parameterContext.putParameter("age", 14);
        }
    }

}
