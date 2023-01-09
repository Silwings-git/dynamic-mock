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
import top.silwings.core.handler.JsonNodeParser;
import top.silwings.core.handler.MockHandler;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.response.MockResponseInfoFactory;
import top.silwings.core.handler.task.MockTaskInfoFactory;
import top.silwings.core.handler.task.MockTaskManager;
import top.silwings.core.handler.tree.dynamic.AutoTypeParser;
import top.silwings.core.handler.tree.dynamic.DynamicExpressionStringParser;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.handler.tree.dynamic.expression.ExpressionDynamicValueFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionDynamicValueFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.ToJsonStringFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.ConcatFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.ContainsFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.EqualsFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.IsBlankFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.IsEmptyFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.IsNotBlankFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.IsNotEmptyFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.IsNotNullFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.IsNullFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.JoinFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.NoEqualsFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.NowFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.PageFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.PrintFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.RandomFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.SearchFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.SelectFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.ToBeanFunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.functions.UUIDFunctionFactory;
import top.silwings.core.handler.tree.dynamic.operator.OperationDynamicValueFactory;
import top.silwings.core.handler.tree.dynamic.operator.OperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.AdditionOperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.AndOperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.ArithmeticEqualOperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.ArithmeticNoEqualOperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.DivisionOperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.GreaterEqualOperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.GreaterOperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.LessEqualOperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.LessOperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.MultiplicationOperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.OrDoubleOperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.RemainderOperatorFactory;
import top.silwings.core.handler.tree.dynamic.operator.operators.SubtractionOperatorFactory;
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
        private final OperationDynamicValueFactory operationDynamicValueFactory;
        private final ExpressionDynamicValueFactory expressionFactory;
        private final FunctionDynamicValueFactory functionFactory;
        private final DynamicValueFactory dynamicValueFactory;
        private final JsonNodeParser jsonNodeParser;
        private final MockResponseInfoFactory mockResponseInfoFactory;
        private final MockTaskInfoFactory mockTaskInfoFactory;
        private final MockHandlerFactory mockHandlerFactory;
        private final DynamicMockContext dynamicMockContext;
        private final MockTaskManager mockTaskManager;
        private final WebClient webClient;

        public ApplicationContent() {
            this.dynamicExpressionStringParser = new DynamicExpressionStringParser();
            this.autoTypeParser = new AutoTypeParser(new DynamicExpressionStringParser());
            this.operationDynamicValueFactory = new OperationDynamicValueFactory(this.loadOperation());
            this.expressionFactory = new ExpressionDynamicValueFactory(this.dynamicExpressionStringParser, this.autoTypeParser, this.operationDynamicValueFactory);
            this.functionFactory = new FunctionDynamicValueFactory(this.loadFunction());
            this.dynamicValueFactory = new DynamicValueFactory(this.expressionFactory, this.functionFactory);
            this.jsonNodeParser = new JsonNodeParser(this.dynamicValueFactory);
            this.mockResponseInfoFactory = new MockResponseInfoFactory(this.dynamicValueFactory, this.jsonNodeParser);
            this.mockTaskInfoFactory = new MockTaskInfoFactory(this.dynamicValueFactory, this.jsonNodeParser);
            this.mockHandlerFactory = new MockHandlerFactory(this.jsonNodeParser, this.mockResponseInfoFactory, this.mockTaskInfoFactory);
            this.mockTaskManager = new MockTaskManager(new ThreadPoolTaskScheduler(), new TaskSchedulerProperties());
            this.webClient = WebClient.builder().defaultHeader("Requester", "Dynamic-Mock-Service").build();
            this.dynamicMockContext = new DynamicMockContext(this.mockTaskManager, new SimpleIdGenerator(), this.jsonNodeParser, this.dynamicValueFactory, this.functionFactory, webClient, obj -> {
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