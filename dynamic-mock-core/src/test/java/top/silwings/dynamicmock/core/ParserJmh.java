package top.silwings.dynamicmock.core;

import lombok.Getter;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.JsonNodeParser;
import top.silwings.core.handler.ParameterContext;
import top.silwings.core.handler.dynamic.DynamicValue;
import top.silwings.core.handler.dynamic.DynamicValueFactory;
import top.silwings.core.handler.node.Node;

/**
 * @ClassName ParserJmh
 * @Description
 * @Author Silwings
 * @Date 2022/11/9 21:16
 * @Since
 **/
public class ParserJmh {

    @Getter
    @State(Scope.Benchmark)
    public static class TestData {
        private final String test001 = "#search(#search(#search(#search(param)+(20-#search(paramA)--1-2))))";
        private final String test002 = "{\n" +
                "\"id\":\"${#uuid()}\"," +
                "\"name\": \"${#search(#search(3*(1+1)--2-6))}\"," +
                "\"age\": \"${#search(age)}\"," +
                "\"happy\": \"${             10  + 1-1        == 11-1 }\"" +
                "}";
        private final JsonNodeParser jsonNodeParser = new JsonNodeParser(new DynamicValueFactory());
        private final ParameterContext context1 = new ParameterContext();
        private final ParameterContext context2 = new ParameterContext();

        private final Node analyzeNode = this.getJsonNodeParser().parse(this.getTest002());

        final DynamicValue dynamicValue = new DynamicValueFactory().buildDynamicValue(this.getTest001());

        public TestData() {

            this.context1.putParameter("paramA", -1);
            this.context1.putParameter("param", 20);
            this.context1.putParameter("40", true);
            this.context1.putParameter("age", 18);
            this.context1.putParameter("2", 15);
            this.context1.putParameter("true", 10);
            this.context1.putParameter("10", "御坂美琴");

            this.context2.putParameter("param", 1);
            this.context2.putParameter("2", "name");
            this.context2.putParameter("name", "御坂美琴");
            this.context2.putParameter("age", 14);
        }
    }

    @Benchmark
    public void test101(final TestData testData) {

        final DynamicValue dynamicValue = new DynamicValueFactory().buildDynamicValue(testData.getTest001());

        dynamicValue.value(testData.getContext1());
    }

    @Benchmark
    public void test102(final TestData testData) {

        testData.getDynamicValue().value(testData.getContext1());
    }

    @Benchmark
    public void test201(final TestData testData) {

        final Node analyze1 = testData.getJsonNodeParser().parse(testData.getTest002());

        final Context context = new Context(testData.getTest002().length(), testData.getContext2());

        analyze1.interpret(context);
    }

    @Benchmark
    public void test202(final TestData testData) {

        final Context context = new Context(testData.getTest002().length(), testData.getContext2());

        testData.getAnalyzeNode().interpret(context);
    }

}