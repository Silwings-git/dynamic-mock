package top.silwings.admin.web;

import lombok.Getter;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.SimpleIdGenerator;
import org.springframework.web.reactive.function.client.WebClient;
import top.silwings.admin.web.setup.MockHandlerDefinitionMock;
import top.silwings.core.config.DynamicMockContext;
import top.silwings.core.config.MockTaskLogProperties;
import top.silwings.core.config.TaskSchedulerProperties;
import top.silwings.core.handler.MockHandler;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.check.CheckInfoFactory;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.handler.response.MockResponseInfoFactory;
import top.silwings.core.handler.task.MockTaskInfoFactory;
import top.silwings.core.handler.task.MockTaskManager;
import top.silwings.core.interpreter.ExpressionInterpreter;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.DynamicExpressionFactory;
import top.silwings.core.interpreter.dynamic_expression.ExpressionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionExpressionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.ConcatFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.ContainsFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.EqualsFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.IsBlankFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.IsEmptyFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.IsNotBlankFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.IsNotEmptyFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.IsNotNullFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.IsNullFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.JoinFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.NoEqualsFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.NowFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.PageFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.ParseJsonStringFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.PrintFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.RandomFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.SaveCacheFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.SearchFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.SelectIfFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.TimeShiftFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.ToBeanFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.ToJsonStringFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.URLDecodeFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.URLEncodeFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.UUIDFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.OperatorExpressionFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.OperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.AdditionOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.AndOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.ArithmeticEqualOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.ArithmeticNoEqualOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.DivisionOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.GreaterEqualOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.GreaterOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.LessEqualOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.LessOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.MultiplicationOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.OrDoubleOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.RemainderOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.operator.operator_factory.SubtractionOperatorFactory;
import top.silwings.core.interpreter.dynamic_expression.parser.AutoTypeParser;
import top.silwings.core.interpreter.dynamic_expression.parser.DynamicExpressionStringParser;
import top.silwings.core.interpreter.json.JsonTreeParser;
import top.silwings.core.model.MockHandlerDto;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName ParserJmh
 * @Description
 * @Author Silwings
 * @Date 2022/11/9 21:16
 * @Since
 **/
@Fork(3)
@Warmup(iterations = 2, time = 10)
@Measurement(iterations = 2, time = 10)
public class MockHandlerJmh {

    @Benchmark
    public void test202(final TestData testData) {
        testData.getStaticPageMockHandler().mock(MockHandlerContext.from(testData.getRequest()));
    }

    @Benchmark
    public void test203(final TestData testData) {

        final String str = "#page(1,10,100,'{\"code\": \"CD001\",\"status\": \"${#search(^'$.list6[^'+#search(#saveCache(^'index^',#search(^'index^',^'localCache^',-1)+1,^'key^'),^'localCache^')+^']^',^'customizeSpace^')}\"}')";

        final ExpressionTreeNode functionExpression = testData.getApplicationContent().getDynamicExpressionFactory().buildDynamicValue(str);

        new ExpressionInterpreter(functionExpression).interpret(MockHandlerContext.from(testData.getRequest()));
    }

    @Benchmark
    public void test204(final TestData testData) {
        testData.getPageExpressionInterpreter().interpret(MockHandlerContext.from(testData.getRequest()));
    }

    @Benchmark
    public void test205(final TestData testData) {

        final String str = "#page(1,10,100,'{\"code\": \"CD001\",\"status\": \"${#search(^'$.list6[^'+#search(#saveCache(^'index^',#search(^'index^',^'localCache^',-1)+1,^'key^'),^'localCache^')+^']^',^'customizeSpace^')}\"}')";

        final ExpressionTreeNode functionExpression = testData.getApplicationContent().getDynamicExpressionFactory().buildDynamicValue(str);

        new ExpressionInterpreter(functionExpression);
    }

    @Getter
    @State(Scope.Benchmark)
    public static class TestData {

        private final HttpServletRequest request;

        private final MockHandler staticPageMockHandler;

        private final MockHandlerJmh.ApplicationContent applicationContent;

        private final ExpressionInterpreter pageExpressionInterpreter;

        public TestData() {

            this.applicationContent = new MockHandlerJmh.ApplicationContent();

            final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
            mockRequest.setMethod("POST");
            mockRequest.setRequestURI("/jmh");
            mockRequest.setContent("{\"pageNum\": \"1\",\"pageSize\": \"10\"}".getBytes(StandardCharsets.UTF_8));
            this.request = mockRequest;

            final MockHandlerDto staticPage = MockHandlerDefinitionMock.build();
            this.staticPageMockHandler = this.applicationContent.getMockHandlerFactory().buildMockHandler(staticPage);

            final String str = "#page(1,10,100,'{\"code\": \"CD001\",\"status\": \"${#search(^'$.list6[^'+#search(#saveCache(^'index^',#search(^'index^',^'localCache^',-1)+1,^'key^'),^'localCache^')+^']^',^'customizeSpace^')}\"}')";
            final ExpressionTreeNode functionExpression = this.applicationContent.getDynamicExpressionFactory().buildDynamicValue(str);
            this.pageExpressionInterpreter = new ExpressionInterpreter(functionExpression);
        }
    }

    @Getter
    public static class ApplicationContent {
        private final DynamicExpressionStringParser dynamicExpressionStringParser;
        private final AutoTypeParser autoTypeParser;
        private final OperatorExpressionFactory operatorExpressionFactory;
        private final ExpressionFactory expressionFactory;
        private final FunctionExpressionFactory functionFactory;
        private final DynamicExpressionFactory dynamicExpressionFactory;
        private final JsonTreeParser jsonTreeParser;
        private final MockResponseInfoFactory mockResponseInfoFactory;
        private final MockTaskInfoFactory mockTaskInfoFactory;
        private final CheckInfoFactory checkInfoFactory;
        private final MockHandlerFactory mockHandlerFactory;
        private final DynamicMockContext dynamicMockContext;
        private final MockTaskManager mockTaskManager;
        private final WebClient webClient;

        public ApplicationContent() {
            this.dynamicExpressionStringParser = new DynamicExpressionStringParser();
            this.autoTypeParser = new AutoTypeParser(new DynamicExpressionStringParser());
            this.operatorExpressionFactory = new OperatorExpressionFactory(this.loadOperation());
            this.expressionFactory = new ExpressionFactory(this.dynamicExpressionStringParser, this.autoTypeParser, this.operatorExpressionFactory);
            this.functionFactory = new FunctionExpressionFactory(this.loadFunction());
            this.dynamicExpressionFactory = new DynamicExpressionFactory(this.expressionFactory, this.functionFactory);
            this.jsonTreeParser = new JsonTreeParser(this.dynamicExpressionFactory);
            this.checkInfoFactory = new CheckInfoFactory(this.dynamicExpressionFactory);
            this.mockResponseInfoFactory = new MockResponseInfoFactory(this.dynamicExpressionFactory, this.jsonTreeParser, checkInfoFactory);
            this.mockTaskInfoFactory = new MockTaskInfoFactory(this.dynamicExpressionFactory, this.jsonTreeParser);
            this.mockHandlerFactory = new MockHandlerFactory(this.jsonTreeParser, this.mockResponseInfoFactory, this.mockTaskInfoFactory, null, this.checkInfoFactory);
            final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
            taskScheduler.initialize();
            this.mockTaskManager = new MockTaskManager(taskScheduler, new TaskSchedulerProperties());
            this.webClient = WebClient.builder().defaultHeader("Requester", "Dynamic-Mock-Service").build();
            this.dynamicMockContext = new DynamicMockContext(this.mockTaskManager, new SimpleIdGenerator(), this.jsonTreeParser, this.dynamicExpressionFactory, this.functionFactory, webClient, obj -> {
            }, new MockTaskLogProperties());
            this.dynamicMockContext.afterPropertiesSet();
        }

        private List<FunctionFactory> loadFunction() {
            final IsBlankFunctionFactory isBlankFunctionFactory = new IsBlankFunctionFactory();
            final IsEmptyFunctionFactory isEmptyFunctionFactory = new IsEmptyFunctionFactory();
            final IsNullFunctionFactory isNullFunctionFactory = new IsNullFunctionFactory();
            final EqualsFunctionFactory equalsFunctionFactory = new EqualsFunctionFactory();
            return Stream.of(
                            new ConcatFunctionFactory(),
                            new ContainsFunctionFactory(),
                            equalsFunctionFactory,
                            isBlankFunctionFactory,
                            isEmptyFunctionFactory,
                            new IsNotBlankFunctionFactory(isBlankFunctionFactory),
                            new IsNotEmptyFunctionFactory(isEmptyFunctionFactory),
                            new IsNotNullFunctionFactory(isNullFunctionFactory),
                            isNullFunctionFactory,
                            new JoinFunctionFactory(),
                            new NoEqualsFunctionFactory(equalsFunctionFactory),
                            new NowFunctionFactory(),
                            new PageFunctionFactory(),
                            new PrintFunctionFactory(),
                            new RandomFunctionFactory(),
                            new SearchFunctionFactory(),
                            new ToBeanFunctionFactory(),
                            new ToJsonStringFunctionFactory(),
                            new UUIDFunctionFactory(),
                            new SelectIfFunctionFactory(),
                            new ParseJsonStringFunctionFactory(),
                            new URLDecodeFunctionFactory(),
                            new URLEncodeFunctionFactory(),
                            new SaveCacheFunctionFactory(),
                            new TimeShiftFunctionFactory()
                    )
                    .collect(Collectors.toList());
        }

        private List<OperatorFactory> loadOperation() {
            final ArithmeticEqualOperatorFactory arithmeticEqualOperatorFactory = new ArithmeticEqualOperatorFactory();
            return Stream.of(
                    new AdditionOperatorFactory(),
                    new AndOperatorFactory(),
                    arithmeticEqualOperatorFactory,
                    new ArithmeticNoEqualOperatorFactory(arithmeticEqualOperatorFactory),
                    new DivisionOperatorFactory(),
                    new GreaterEqualOperatorFactory(),
                    new GreaterOperatorFactory(),
                    new LessEqualOperatorFactory(),
                    new LessOperatorFactory(),
                    new MultiplicationOperatorFactory(),
                    new OrDoubleOperatorFactory(),
                    new RemainderOperatorFactory(),
                    new SubtractionOperatorFactory()
            ).collect(Collectors.toList());
        }
    }

}