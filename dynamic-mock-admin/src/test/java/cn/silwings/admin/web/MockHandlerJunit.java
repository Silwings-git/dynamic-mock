package cn.silwings.admin.web;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SimpleIdGenerator;
import org.springframework.web.reactive.function.client.WebClient;
import cn.silwings.admin.DynamicMockAdminApplication;
import cn.silwings.admin.web.setup.MockHandlerDefinitionMock;
import cn.silwings.core.config.DynamicMockContext;
import cn.silwings.core.config.MockTaskLogProperties;
import cn.silwings.core.config.TaskSchedulerProperties;
import cn.silwings.core.handler.MockHandler;
import cn.silwings.core.handler.MockHandlerFactory;
import cn.silwings.core.handler.check.CheckInfoFactory;
import cn.silwings.core.handler.context.MockHandlerContext;
import cn.silwings.core.handler.context.MockPluginContext;
import cn.silwings.core.handler.plugin.MockHandlerPluginInfo;
import cn.silwings.core.handler.plugin.MockPluginInfo;
import cn.silwings.core.handler.plugin.PluginInterfaceType;
import cn.silwings.core.handler.plugin.PluginRegistrationProgram;
import cn.silwings.core.handler.plugin.PluginRegistrationProgramManager;
import cn.silwings.core.handler.plugin.executors.PluginExecutor;
import cn.silwings.core.handler.response.MockResponseInfoFactory;
import cn.silwings.core.handler.task.MockTaskInfoFactory;
import cn.silwings.core.handler.task.MockTaskManager;
import cn.silwings.core.interpreter.dynamic_expression.DynamicExpressionFactory;
import cn.silwings.core.interpreter.dynamic_expression.ExpressionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionExpressionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.ConcatFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.ContainsFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.EqualsFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.IsBlankFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.IsEmptyFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.IsNotBlankFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.IsNotEmptyFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.IsNotNullFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.IsNullFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.JoinFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.NoEqualsFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.NowFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.PageFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.ParseJsonStringFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.PrintFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.RandomFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.SaveCacheFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.SearchFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.SelectIfFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.TimeFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.TimeShiftFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.ToBeanFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.ToJsonStringFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.URLDecodeFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.URLEncodeFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.function.function_factory.UUIDFunctionFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.OperatorExpressionFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.OperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.AdditionOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.AndOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.ArithmeticEqualOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.ArithmeticNoEqualOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.DivisionOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.GreaterEqualOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.GreaterOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.LessEqualOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.LessOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.MultiplicationOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.OrDoubleOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.RemainderOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.operator.operator_factory.SubtractionOperatorFactory;
import cn.silwings.core.interpreter.dynamic_expression.parser.AutoTypeParser;
import cn.silwings.core.interpreter.dynamic_expression.parser.DynamicExpressionStringParser;
import cn.silwings.core.interpreter.json.JsonTreeParser;
import cn.silwings.core.model.MockHandlerDto;
import cn.silwings.core.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
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
@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = DynamicMockAdminApplication.class)
public class MockHandlerJunit {

    @Test
    public void test202() {

        final TestData testData = new TestData();

        final ResponseEntity<?> mock = testData.getMockHandler().mock(MockHandlerContext.from(testData.getRequest()));

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

    @Component
    public static class PrintSomethingPlugin implements PluginRegistrationProgram {
        @Override
        public PluginExecutor<?> newPluginExecutor(final MockHandlerPluginInfo mockHandlerPluginInfo, final MockHandlerDto definition) {
            return new PluginExecutor<Void>() {
                @Override
                public PluginInterfaceType getPluginInterfaceType() {
                    return PluginInterfaceType.PRE_MOCK;
                }

                @Override
                public void close() {
                }

                @Override
                public int getOrder() {
                    return 0;
                }

                @Override
                public Void execute(final MockPluginContext mockPluginContext) {
                    System.out.println("JAVA插件被执行.");
                    return null;
                }
            };
        }

        @Override
        public MockPluginInfo getMockPluginInfo() {
            return MockPluginInfo.builder()
                    .pluginCode("anonymousPlugin")
                    .pluginName("匿名测试插件")
                    .build();
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
        private final PluginRegistrationProgramManager pluginRegistrationProgramManager;

        public ApplicationContent() {

            this.dynamicExpressionStringParser = new DynamicExpressionStringParser();
            this.autoTypeParser = new AutoTypeParser(new DynamicExpressionStringParser());
            this.operatorExpressionFactory = new OperatorExpressionFactory(this.loadOperation());
            this.expressionFactory = new ExpressionFactory(this.dynamicExpressionStringParser, this.autoTypeParser, this.operatorExpressionFactory);
            this.functionFactory = new FunctionExpressionFactory(this.loadFunction());
            this.dynamicExpressionFactory = new DynamicExpressionFactory(this.expressionFactory, this.functionFactory);
            this.jsonTreeParser = new JsonTreeParser(this.dynamicExpressionFactory);
            this.checkInfoFactory = new CheckInfoFactory(this.dynamicExpressionFactory);
            this.mockResponseInfoFactory = new MockResponseInfoFactory(this.dynamicExpressionFactory, this.jsonTreeParser, this.checkInfoFactory);
            this.mockTaskInfoFactory = new MockTaskInfoFactory(this.dynamicExpressionFactory, this.jsonTreeParser);
            this.pluginRegistrationProgramManager = new PluginRegistrationProgramManager(Collections.emptyList());
            this.mockHandlerFactory = new MockHandlerFactory(this.jsonTreeParser, this.mockResponseInfoFactory, this.mockTaskInfoFactory, this.checkInfoFactory, this.pluginRegistrationProgramManager);
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
                            new TimeShiftFunctionFactory(),
                            new TimeFunctionFactory()
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