package top.silwings.admin.web;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.SimpleIdGenerator;
import org.springframework.web.reactive.function.client.WebClient;
import top.silwings.admin.web.setup.MockHandlerDefinitionMock;
import top.silwings.core.config.DynamicMockContext;
import top.silwings.core.config.MockTaskLogProperties;
import top.silwings.core.config.TaskSchedulerProperties;
import top.silwings.core.handler.MockHandler;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.response.MockResponseInfoFactory;
import top.silwings.core.handler.task.MockTaskInfoFactory;
import top.silwings.core.handler.task.MockTaskManager;
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
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.PrintFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.RandomFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.SearchFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.SelectFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.ToBeanFunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.function_factory.ToJsonStringFunctionFactory;
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
import top.silwings.core.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName MockHandlerJunit
 * @Description
 * @Author Silwings
 * @Date 2023/1/5 16:45
 * @Since
 **/
@Slf4j
//@RunWith(value = SpringRunner.class)
//@SpringBootTest(classes = DynamicMockAdminApplication.class)
public class MockHandlerJunit {

    @Test
    public void test202() {

        final TestData testData = new TestData();

        final ResponseEntity<Object> mock = testData.getMockHandler().mock(MockHandlerContext.from(testData.getRequest()));

        log.info("Result: {}", JsonUtils.toJSONString(mock));
    }

    @Getter
    public static class TestData {

        private final HttpServletRequest request;

        private final MockHandler mockHandler;

        private final MockHandlerJmh.ApplicationContent applicationContent;

        public TestData() {

            this.applicationContent = new MockHandlerJmh.ApplicationContent();

            final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
            mockRequest.setMethod("POST");
            mockRequest.addHeader("Content-Type", "application/json");
            mockRequest.setRequestURI("/jmh");
            mockRequest.setContent("{\"pageNum\": \"1\",\"pageSize\": \"10\"}".getBytes(StandardCharsets.UTF_8));
            this.request = mockRequest;

            final MockHandlerDto definition = MockHandlerDefinitionMock.build();
            this.mockHandler = this.applicationContent.getMockHandlerFactory().buildMockHandler(definition);
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
            this.mockResponseInfoFactory = new MockResponseInfoFactory(this.dynamicExpressionFactory, this.jsonTreeParser);
            this.mockTaskInfoFactory = new MockTaskInfoFactory(this.dynamicExpressionFactory, this.jsonTreeParser);
            this.mockHandlerFactory = new MockHandlerFactory(this.jsonTreeParser, this.mockResponseInfoFactory, this.mockTaskInfoFactory);
            this.mockTaskManager = new MockTaskManager(new ThreadPoolTaskScheduler(), new TaskSchedulerProperties());
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
                            new SelectFunctionFactory()
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