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
import top.silwings.admin.web.setup.MockHandlerDefinitionMock;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.MockEndPoint;
import top.silwings.core.handler.MockHandler;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.handler.context.RequestContext;
import top.silwings.core.interpreter.ExpressionInterpreter;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.TreeNodeReader;
import top.silwings.core.interpreter.dynamic_expression.DynamicExpressionFactory;
import top.silwings.core.interpreter.dynamic_expression.parser.DynamicExpressionStringParser;
import top.silwings.core.interpreter.json.JsonTreeParser;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.model.TaskRequestDto;
import top.silwings.core.utils.JsonUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest(classes = DynamicMockAdminApplication.class)
@RunWith(SpringRunner.class)
public class ParserTest {

    @Autowired
    private DynamicExpressionFactory dynamicExpressionFactory;

    @Autowired
    private JsonTreeParser jsonTreeParser;
    @Autowired
    private MockHandlerManager mockHandlerManager;
    @Autowired
    private MockHandlerFactory mockHandlerFactory;
    @Autowired
    private MockEndPoint mockEndPoint;

    @Autowired
    private DynamicExpressionStringParser dynamicExpressionStringParser;

    @Test
    public void test001() {

        final List<String> expressionList = new ArrayList<>();

        expressionList.add("1+2");
        expressionList.add("1+2*3");
        expressionList.add("2+#search('age')");
        expressionList.add("2+#search('age')*2");
        expressionList.add("(2+#search('age'))*2+2*4");
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
        expressionList.add("#search(#search(#search(#search('param')+#search('param'))))");
        expressionList.add("#search(#search(#search(#search('param')+(19-#search('paramA')))))");
        expressionList.add("#search(#search(#search(#search('param')+(20-#search('paramA')--1-2))))");
        expressionList.add("#search('abc.abc')");
        expressionList.add("#isBlank('abc.abc')");
        expressionList.add("#isBlank('abc.abc')");
        expressionList.add("#isBlank()");
        expressionList.add("#isBlank('')");
        expressionList.add("#isBlank() && true");
        expressionList.add("#isBlank() && false");
        expressionList.add("#search(#isBlank())");
        expressionList.add("#search(10+#isBlank())");
        expressionList.add("'2+2'");
        expressionList.add("2+2");
        expressionList.add("'2+2'");
        expressionList.add("#eq(#uuid(),1)");
        expressionList.add("#eq(#search('abcMap.list[0],customizeSpace'),-1)");
        expressionList.add("#uuid(,,)");
        expressionList.add("#uuid(,,false)");
        expressionList.add("#uuid(,,true)");
        expressionList.add("#uuid(,10)");
        expressionList.add("#uuid(,10,true)");
        // 触发表达式解析异常
//        expressionList.add("#uuid(UserCode-,10,true)");
//        expressionList.add("#uuid(<UserCode->,10,true)");

        expressionList.add("             10  + 1-1        == 11-1 ");
        expressionList.add("10+1-1==11-1");
        expressionList.add("#eq(#eq(1,2),false)");
        expressionList.add("#eq(#eq(1,2),true)");
        expressionList.add("#random()");
        expressionList.add("#random('int')");
        expressionList.add("#random('int',1)");
        expressionList.add("#random('int',1,10)");
        expressionList.add("#random('long')");
        expressionList.add("#random('long',1)");
        expressionList.add("#random('long',1,10)");
        expressionList.add("#random('double')");
        expressionList.add("#random('double',1)");
        expressionList.add("#random('double',1.9999999999999999,2.000000000000001)");
        expressionList.add("#random('boolean')");
        expressionList.add("#random('boolean',1)");
        expressionList.add("#random('boolean',1,2)");
        expressionList.add("(1+2)-(2*4)");
        expressionList.add("((1+2)-(2*4)+5)+(4+6)");
        expressionList.add("#join(,1,2,3,4,,5)");
        expressionList.add("#join('-',1,2,3,4,,5)");
        expressionList.add("#concat(,1,2,3,4,,5)");
        expressionList.add("#concat('-',1,2,3,4,,5)");
        expressionList.add("#join('-',1,2)");
        expressionList.add("#join('-',)");
        expressionList.add("#join('#',1,,2,,3)");
        expressionList.add("#now()");
        expressionList.add("#now('yyyy-MM-dd')");
        expressionList.add("#now('yyyy-MM-dd HH:mm:ss')");
        expressionList.add("#now('yyyy')");

        expressionList.add("#page(1,2,101,'{\"name\":88}')");
        expressionList.add("#page(1,2,101,'{\"name\":\"${#search(^'age^')>0}\"}')");
        expressionList.add("#page(1,2,101,'{\"name\":\"#search(^'age^')\"}')");
        expressionList.add("#page(1,2,101,'1+1')");
        expressionList.add("#page(1,2,101,1+1)");

        expressionList.add("#equals(#search('baseUser.name','customizeSpace'), 'Misaka Mikoto' )");
        expressionList.add("#equals(#search('baseUser.name','customizeSpace'),'Misaka Mikoto')");
        expressionList.add("#equals(1,1 -  1+1)");
        expressionList.add("#equals(              1        ,(              1 -  1        )     +             1 )");
        expressionList.add("#equals(           1         ,                1         - (1                   + 1                   )               )");
        expressionList.add("#page(1,10,#search('list'))");
        expressionList.add("#page(1,10,#search('list2'))");
        expressionList.add("#page(1,2,'[{\"name\":\"御坂美琴\",\"age\":14},{\"name\":\"御坂美琴\",\"age\":15},{\"name\":\"御坂美琴\",\"age\":16}]')");
        expressionList.add("#page(2,2,'[{\"name\":\"御坂美琴\",\"age\":14},{\"name\":\"御坂美琴\",\"age\":15},{\"name\":\"御坂美琴\",\"age\":\"${#search(^'param^')}\"}]')");
        expressionList.add("#page(3,2,'[{\"name\":\"御坂美琴\",\"age\":14},{\"name\":\"御坂美琴\",\"age\":15},{\"name\":\"御坂美琴\",\"age\":16}]')");
        expressionList.add("#page(1,2,101,'${1+1}')");
        expressionList.add("#page(1,2,101,'${1+1}',false)");
        expressionList.add("#uuid(,,true)");
        expressionList.add("#uuid(,10,true)");
        expressionList.add("#uuid(,40,true)");
        expressionList.add("#uuid('UserCode',,false)");
        expressionList.add("#uuid('UserCode',10,true)");
        expressionList.add("#isNotNull()");
        expressionList.add("#isNotNull('')");
        expressionList.add("#isNull()");
        expressionList.add("#isNull('')");
        expressionList.add("#search('$[0]',#search('list2'))");
        expressionList.add("#search('$.name',#search('$[0]',#search('list2')))");
        expressionList.add("#search('$.page',#search('page_param'))");
        expressionList.add("#isNull(2)");
        expressionList.add("#isNull('3')");
        expressionList.add("#isNull(3.4)");
        expressionList.add("#isNull(3.4+1.6)");
        expressionList.add("#isEmpty('[]')");
        expressionList.add("#isEmpty('[1]')");
        expressionList.add("#random()");
        expressionList.add("#random('boolean')");
        expressionList.add("#random('int',100)");
        expressionList.add("#random('int',10,20)");
        expressionList.add("#Contains('a','abc')");
        expressionList.add("#Contains('abc','a')");
        expressionList.add("#Contains(#search('list3'),'a')");
        expressionList.add("#Contains(#search('list3'),'2')");
        expressionList.add("#Contains(#search('list3'),2)");
        expressionList.add("#Contains(#search('list4'),'2')");
        expressionList.add("#Contains(#search('list4'),2)");
        expressionList.add("#Contains(#search('list4'),#search('list5'))");
        expressionList.add("#Contains(#toBean('[1,2,3]'),1)");
        expressionList.add("#Contains(#toBean('[1,2,3]'),'1')");
        expressionList.add("#Contains(#toBean('[1,2,3]'),#toBean('[1,3]'))");
        expressionList.add("#Contains(#toBean('[1,2,3]'),#toBean('[1,\"3\"]'))");
        expressionList.add("#Contains(#toBean('[1,2,\"3\"]'),#toBean('[1,\"3\"]'))");
        expressionList.add("#toBean('{\"name\":\"御坂美琴\"}')");
        expressionList.add("#toBean('[\"1\",\"2\"]')");
        expressionList.add("#tjs(#toBean('{\"name\":\"御坂美琴\"}'))");
        expressionList.add("#print(#toBean('{\"name\":\"御坂美琴\"}'))");
        expressionList.add("#print(#tjs(#toBean('{\"name\":\"御坂美琴\"}')))");
        expressionList.add("3*(1+1)");
        expressionList.add("3*(1+1)--2-6");
        expressionList.add("#search(#print(3*(1+1)--2-6))");
        expressionList.add("#page(1,2,101,'{\"name\":\"${#search(^'10true^')}\"}',true)");
        expressionList.add("#search('param','customizeSpace')");
//        expressionList.add("#page(#search('$.body.pageNum','requestInfo'),#search('$.body.pageSize','requestInfo'),101,'{\\\"code\\\": \\\"CD001\\\",\\\"status\\\": \\\"2\\\"}')");
        expressionList.add("#page(#search('$.pageNum'),#search('$.pageSize'),101,'{\"code\": \"CD001\",\"status\": \"2\"}')");
        expressionList.add("#search('abcabcabc','customizeSpace','御坂美琴')");
        expressionList.add("#search('abcabcabc','localcache','Misaka Mikoto')");
        expressionList.add("#search(#saveCache('abcabcabc','abcabcabc'),'localcache','Misaka Mikoto')");
        expressionList.add("#search(#saveCache('abcabcabc','御坂美琴','key'),'localcache','Misaka Mikoto')");
        expressionList.add("#print(#saveCache('abcabcabc','御坂美琴','key'))");
        expressionList.add("#print(#saveCache('abcabcabc','御坂美琴','value'))");
        expressionList.add("#print(#saveCache('abcabcabc','御坂美琴','all'))");
        expressionList.add("#print(#search('$.list6['+#search(#saveCache('index',#search('index','localCache',-1)+1,'key'),'localCache')+']','customizeSpace'))");
        expressionList.add("#print(#search('$.list6['+#search(#saveCache('index',#search('index','localCache',-1)+1,'key'),'localCache')+']','customizeSpace'))" +
                           "+#print(#search('$.list6['+#search(#saveCache('index',#search('index','localCache',-1)+1,'key'),'localCache')+']','customizeSpace'))");
        // 遍历集合
        expressionList.add("#page(1,10,100,'${#search(^'$.list6[^'+#search(#saveCache(^'index^',#search(^'index^',^'localCache^',-1)+1,^'key^'),^'localCache^')+^']^',^'customizeSpace^')}')");
        expressionList.add("#page(#search('$.pageNum'),#search('$.pageSize'),101,'{\"code\": \"CD001\",\"status\": \"${#search(^'$.list6[^'+#search(#saveCache(^'index^',#search(^'index^',^'localCache^',-1)+1,^'key^'),^'localCache^')+^']^',^'customizeSpace^')}\"}')");
        expressionList.add("#select(true,1,2)");
        expressionList.add("#select(false,1,2)");
        expressionList.add("#select(#search('40'),1,2)");
        expressionList.add("#select(10>0,1,2)");
        expressionList.add("#toJsonString(1+1)");
        expressionList.add("#toJsonString('{\"name\":\"aka Mikoto\"}')");
        expressionList.add("#toJsonString(#search('user'))");
        expressionList.add("#parseJsonString('{\"name\":\"御坂美琴\"}')");
        expressionList.add("#parseJsonString('{\"name\":\"${#search(10,^'customizespace^')}\"}')");
        expressionList.add("#parseJsonString('[{\"name\":\"${#search(10,^'customizespace^')}\"}]')");
        // page函数返回的数据类型不是string,这里使用toJsonString函数将对象转字符串再交给parseJsonString处理
        expressionList.add("#parseJsonString(#toJsonString(#page(1,2,101,'{\"name\":\"${#search(^'10true^')}\",\"uid\":\"${#uuid()}\"}',true)))");
        expressionList.add("#parseJsonString('{\"name\":\"御坂美琴\"}',false)");
        expressionList.add("#parseJsonString('{\"name\":\"${#search(10,^'customizespace^')}\"}',false)");
        expressionList.add("#parseJsonString('[{\"name\":\"${#search(10,^'customizespace^')}\"}]',false)");
        // page函数返回的数据类型不是string,这里使用toJsonString函数将对象转字符串再交给parseJsonString处理
        expressionList.add("#parseJsonString(#toJsonString(#page(1,2,101,'{\"name\":\"${#search(^'10true^')}\",\"uid\":\"${#uuid()}\"}',true)),false)");
        expressionList.add("#parseJsonString(#toJsonString(#page(1,2,101,'{\"name\":\"${#search(^'10true^')}\",\"uid\":\"${#uuid()}\"}',false)),false)");
        expressionList.add("#urlDecode('https://demo.com/demo?&timestamp=2020-05-26+17%3A42%3A14.059%2B0800')");
        expressionList.add("#urlEncode('https://demo.com/demo?&timestamp=2020-05-26 10:30:55 +800')");
        expressionList.add("#Contains('userCode001^,userCode','001^,userCode')");
        expressionList.add("#Contains('^{userCode001^,userCode','001^,userCode')");
//        expressionList.add("#Contains('{不存在userCode^,不存在userCode','100^,不存在userCode')");
        expressionList.add("#Contains('^^userCode001^,userCode','001^,userCode')");
        expressionList.add("#timeshift(,,,)");
        expressionList.add("#timeshift('yyyy-MM-dd HH:mm:ss',,,)");
        expressionList.add("#timeshift('yyyy-MM-dd HH:mm:ss','2023-06-16 12:00:00',,)");
        expressionList.add("#timeshift('yyyy-MM-dd HH:mm:ss','2023-06-16 12:00:00','P10DT-1H-5M5S',)");
        expressionList.add("#timeshift('yyyy-MM-dd^'T^'HH:mm:ss','2023-06-16T12:00:00','P10DT-1H-5M5S','zh_CN')");
        expressionList.add("#timeshift('yyyy-MM-dd^'T^'HH:mm:ss','2023-06-16T12:00:00','P10DT-1H-5M5S','en_US')");
        expressionList.add("#timeshift('yyyy-MM-dd HH:mm:ss','','P10DT-1H-5M5S','zh_CN')");


        final HashMap<String, Object> abcMap = new HashMap<>();
        abcMap.put("list", Collections.singletonList(-1));
        final RequestContext requestContext = RequestContext.builder().customizeSpace(new HashMap<>()).build();
        requestContext.addCustomizeParam("pageNum", 1);
        requestContext.addCustomizeParam("pageSize", 10);
        requestContext.addCustomizeParam("paramA", -1);
        requestContext.addCustomizeParam("abcMap", abcMap);
        requestContext.addCustomizeParam("param", 20);
        requestContext.addCustomizeParam("40", true);
        requestContext.addCustomizeParam("age", 18);
        requestContext.addCustomizeParam("2", 15);
        requestContext.addCustomizeParam("true", 10);
        requestContext.addCustomizeParam("10", "御坂美琴");
        requestContext.addCustomizeParam("10true", "御坂美琴");
        requestContext.addCustomizeParam("list", "[{\"name\":\"御坂美琴\",\"age\":14},{\"name\":\"御坂美琴\",\"age\":14},{\"name\":\"御坂美琴\",\"age\":14}]");
        requestContext.addCustomizeParam("list2", "[{\"name\":\"御坂美琴\",\"age\":\"${#search('age')}\"},{\"name\":\"御坂美琴\",\"age\":14},{\"name\":\"御坂美琴\",\"age\":14}]");
        requestContext.addCustomizeParam("list3", Stream.of(1, 2, 3).collect(Collectors.toList()));
        requestContext.addCustomizeParam("list4", Stream.of("1", "2", "3").collect(Collectors.toList()));
        requestContext.addCustomizeParam("list5", Stream.of("1", "3").collect(Collectors.toList()));
        requestContext.addCustomizeParam("list6", Stream.iterate(0, i -> i + 2).limit(200).collect(Collectors.toList()));
        requestContext.addCustomizeParam("page_param", "{\"page\": \"10\"}");
        requestContext.addCustomizeParam("user", new HashMap<String, String>() {{
            put("name", "Misaka Mikoto");
        }});

        final Map<String, Object> baseUser = new HashMap<>();
        baseUser.put("name", "Misaka Mikoto");

        requestContext.addCustomizeParam("baseUser", baseUser);

        expressionList.forEach(exp -> this.extracted(exp, requestContext));
    }

    private void extracted(final String str, final RequestContext requestContext) {

        final ExpressionTreeNode functionExpression = this.dynamicExpressionFactory.buildDynamicValue(str);

        final MockHandlerContext mockHandlerContext = MockHandlerContext.builder()
                .requestContext(requestContext)
                .build();

        final Object interpret = new ExpressionInterpreter(functionExpression).interpret(mockHandlerContext);
        if (null != interpret) {
            System.out.println(str + "-type: " + interpret.getClass().getName() + "-结果 : " + JsonUtils.toJSONString(interpret));
        } else {
            System.out.println(str + "结果 : 返回null");
        }
    }

    @Test
    public void test002() {

        final TestData testData = new TestData();

        final ExpressionTreeNode analyze1 = this.jsonTreeParser.parse(testData.getTest002());

        final MockHandlerContext mockHandlerContext = MockHandlerContext.builder()
                .requestContext(testData.getRequestContext())
                .build();

        final Object interpret = analyze1.interpret(mockHandlerContext, Collections.emptyList());

        log.info(JsonUtils.toJSONString(interpret));
    }

    @Test
    public void test004() {

        final TestData testData = new TestData();

        final ExpressionTreeNode expression = this.jsonTreeParser.parse(testData.getTest002());

        final List<ExpressionTreeNode> expressionList = TreeNodeReader.postOrderTraversal(expression);

        final Stack<Object> stack = new Stack<>();

        for (final ExpressionTreeNode ele : expressionList) {

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

        final ExpressionTreeNode functionExpression = this.dynamicExpressionFactory.buildDynamicValue(expression);

        final ExpressionInterpreter expressionInterpreter = new ExpressionInterpreter(functionExpression);

        final Object interpret = expressionInterpreter.interpret(MockHandlerContext.builder().build());

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

        final ResponseEntity<Object> responseEntity = this.mockEndPoint.executeMock(request);

        final Object body = responseEntity.getBody();
        log.info("res type: " + body.getClass().getName() + "--" + JsonUtils.toJSONString(body));

//        TimeUnit.SECONDS.sleep(5);
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

    @Test
    public void test008() {

        final String s1 = "#search('name')";
        final String s2 = "#search('name',)";
        final String s3 = "#search(,'name',)";
        final String s4 = "#search('name','customSpace')";
        final String s5 = "#search('name','customSpace')";
        final String s7 = "${#equals(#search('baseUser.name','customizeSpace'), 'Misaka Mikoto' )}";

        System.out.println("s = " + s5);

        final Object str = this.dynamicExpressionStringParser.parse("'234'");
        System.out.println("str = " + str);

    }

    @Getter
    public static class TestData {
        final MockHandlerContext mockHandlerContext;
        private final String test001 = "#search(#search(#search(#search('param')+(20-#search('paramA')--1-2))))";
        private final String test002 = "{\n" +
                                       "\"id\":\"${#uuid('abc')}\"," +
                                       "\"${#search('def')}\": \"${#search(#search(#concat('a',(3*(1+1)--2-6))))}\"," +
                                       "\"${#search('abcabc')}\": \"${#search(#search('a'+(3*(1+1)--2-6)))}\"," +
                                       "\"age\": \"${#search('age')}\"," +
                                       "\"zbd\": \"${#search('zbd')}\"," +
                                       "\"happy\": \"${             10  + 1-1        == 11-1 }\"," +
                                       "\"uuidKey\": \"${#uuid(1,2)}\"" +
                                       "}";
        private final String test004 = "{\n" +
                                       "\"${#search('def')}\": \"${#search(#search(3*(1+1)--2-6))}\"," +
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
