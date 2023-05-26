package top.silwings.core.handler;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import top.silwings.core.common.Identity;
import top.silwings.core.config.DynamicMockContext;
import top.silwings.core.handler.context.FinalRequestContext;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.handler.context.RequestInfo;
import top.silwings.core.handler.plugin.interceptor.MockInterceptorContext;
import top.silwings.core.handler.plugin.interceptor.PreResponseInterceptor;
import top.silwings.core.handler.response.MockResponse;
import top.silwings.core.handler.response.MockResponseInfo;
import top.silwings.core.handler.task.MockTaskInfo;
import top.silwings.core.interpreter.ExpressionInterpreter;
import top.silwings.core.utils.DelayUtils;
import top.silwings.core.utils.PathMatcherUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName MockHandler
 * @Description Mock模拟
 * @Author Silwings
 * @Date 2022/11/10 21:51
 * @Since
 **/
@Getter
@Builder
public class MockHandler {

    /**
     * 唯一标识符
     */
    private final Identity handlerId;

    /**
     * 名称
     */
    private final String name;

    /**
     * 支持的请求方式
     */
    private final List<HttpMethod> httpMethodList;

    /**
     * 支持的请求地址
     */
    private final String requestUri;

    /**
     * 延迟执行时间
     */
    private final int delayTime;

    private final long version;

    /**
     * 自定义空间
     */
    private final ExpressionInterpreter customizeSpaceInterpreter;

    private final List<MockResponseInfo> responseInfoList;

    private final List<MockTaskInfo> syncTaskInfoList;

    private final List<MockTaskInfo> asyncTaskInfoList;

    private final MockInterceptorContext mockInterceptorContext;

    public boolean support(final RequestInfo requestInfo) {
        return PathMatcherUtils.match(this.requestUri, requestInfo.getRequestUri()) && this.httpMethodList.contains(requestInfo.getHttpMethod());
    }

    public ResponseEntity<Object> mock(final MockHandlerContext mockHandlerContext) {

        // 初始化这次mock的全局信息
        this.mockInit(mockHandlerContext);

        final MockWorkflowControl mockWorkflowControl = MockWorkflowControl.build();
        final FinalRequestContext finalRequestContext = mockHandlerContext.getRequestContext().toFinal();

        // 执行mock前置拦截器
        this.executePreMockInterceptor(mockWorkflowControl, finalRequestContext);

        // 前置处理器执行结束后要求中断并返回的,需要按照给出的结果立刻执行响应
        if (mockWorkflowControl.isInterruptAndReturn()) {
            return this.response(mockWorkflowControl.getInterruptResult(), finalRequestContext, mockWorkflowControl);
        }

        // 处理器延迟
        this.delay(mockWorkflowControl.isExecuteHandlerDelay());

        // 注册异步定时任务
        this.registerAsyncTask(mockHandlerContext, mockWorkflowControl);

        // 筛选合适的响应信息
        final MockResponse mockResponse = this.filterMockResponse(mockHandlerContext, mockWorkflowControl);

        // 注册并执行一次性的同步任务
        this.registerDisposableSyncTask(mockHandlerContext, mockWorkflowControl);

        return this.response(mockResponse, finalRequestContext, mockWorkflowControl);
    }

    /**
     * 对这次mock的全局信息进行初始化
     *
     * @param mockHandlerContext mock处理器上下文
     */
    private void mockInit(final MockHandlerContext mockHandlerContext) {
        // 初始化自定义空间
        final Object space = this.customizeSpaceInterpreter.interpret(mockHandlerContext);
        if (space instanceof Map && ((Map<?, ?>) space).size() > 0) {
            final Map<String, Object> customizeSpace = new HashMap<>();
            for (final Map.Entry<?, ?> entry : ((Map<?, ?>) space).entrySet()) {
                customizeSpace.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            mockHandlerContext.getRequestContext().setCustomizeSpace(customizeSpace);
        }
    }

    private void executePreMockInterceptor(final MockWorkflowControl mockWorkflowControl, final FinalRequestContext finalRequestContext) {
        this.mockInterceptorContext.getPreMockInterceptorList()
                .forEach(interceptor -> interceptor.execute(finalRequestContext, mockWorkflowControl));
    }

    /**
     * 执行处理器延迟
     *
     * @param delay 是否按照延迟时间进行延迟
     */
    public MockHandler delay(final boolean delay) {
        if (delay) {
            DelayUtils.delay(this.delayTime, TimeUnit.MILLISECONDS);
        }
        return this;
    }

    private void registerAsyncTask(final MockHandlerContext mockHandlerContext, final MockWorkflowControl mockWorkflowControl) {
        if (mockWorkflowControl.isExecuteAsyncTask()) {
            // 筛选异步定时任务
            this.asyncTaskInfoList.stream()
                    .filter(mockTaskInfo -> mockTaskInfo.support(mockHandlerContext))
                    .forEach(mockTaskInfo ->
                            // 初始化异步定时任务
                            DynamicMockContext.getInstance()
                                    .getMockTaskManager()
                                    .registerAsyncTask(mockTaskInfo.getMockTask(mockHandlerContext))
                    );
        }
    }

    private MockResponse filterMockResponse(final MockHandlerContext mockHandlerContext, final MockWorkflowControl mockWorkflowControl) {
        MockResponse mockResponse = null;

        if (mockWorkflowControl.isExecuteResponse()) {
            // 筛选Response
            for (final MockResponseInfo mockResponseInfo : this.responseInfoList) {
                if (mockResponseInfo.support(mockHandlerContext)) {
                    // -- 初始化Response
                    mockResponse = mockResponseInfo.getMockResponse(mockHandlerContext);
                    break;
                }
            }
        }

        return mockResponse;
    }

    private void registerDisposableSyncTask(final MockHandlerContext mockHandlerContext, final MockWorkflowControl mockWorkflowControl) {
        if (mockWorkflowControl.isExecuteSyncTask()) {
            // 筛选同步task
            this.syncTaskInfoList.stream()
                    .filter(mockTaskInfo -> mockTaskInfo.support(mockHandlerContext))
                    .forEach(mockTaskInfo ->
                            // 同步任务仅执行一次
                            DynamicMockContext.getInstance()
                                    .getMockTaskManager()
                                    .registerDisposableSyncTask(mockTaskInfo.getMockTask(mockHandlerContext))
                    );
        }
    }

    /**
     * 执行响应,如果有响应前拦截器,将会在此处执行
     *
     * @param mockResponse        响应结果
     * @param finalRequestContext mock上下文
     * @param mockWorkflowControl 控制信息
     * @return HTTP响应实例
     */
    private ResponseEntity<Object> response(MockResponse mockResponse, FinalRequestContext finalRequestContext, final MockWorkflowControl mockWorkflowControl) {

        // 执行响应前拦截器
        this.executePreResponseInterceptor(mockResponse, finalRequestContext, mockWorkflowControl);

        if (mockWorkflowControl.isInterruptAndReturn()) {
            mockResponse = mockWorkflowControl.getInterruptResult();
        }

        if (null == mockResponse || !mockWorkflowControl.isExecuteResponse()) {
            return ResponseEntity.ok("No response available!");
        }

        return mockResponse
                .delay(mockWorkflowControl.isExecuteResponseDelay())
                .toResponseEntity();
    }

    /**
     * 执行响应前拦截器.响应前拦截器执行中断返回时不会再次被响应拦截器处理
     *
     * @param mockResponse        原始响应信息
     * @param finalRequestContext mock上下文
     * @param mockWorkflowControl 流程控制
     */
    private void executePreResponseInterceptor(final MockResponse mockResponse, final FinalRequestContext finalRequestContext, final MockWorkflowControl mockWorkflowControl) {
        for (final PreResponseInterceptor interceptor : this.mockInterceptorContext.getPreResponseInterceptorList()) {
            interceptor.execute(mockResponse, finalRequestContext, mockWorkflowControl);
        }
    }

}