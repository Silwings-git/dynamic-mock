package top.silwings.dynamicmock.core;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import top.silwings.core.ParameterContext;
import top.silwings.core.dynamic.DynamicValue;
import top.silwings.core.dynamic.DynamicValueFactory;

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

}
