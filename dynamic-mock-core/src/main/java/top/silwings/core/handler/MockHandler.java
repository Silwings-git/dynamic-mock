package top.silwings.core.handler;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import top.silwings.core.common.Identity;
import top.silwings.core.config.DynamicMockContext;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.handler.context.RequestInfo;
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

    public boolean support(final RequestInfo requestInfo) {
        return PathMatcherUtils.match(this.requestUri, requestInfo.getRequestUri()) && this.httpMethodList.contains(requestInfo.getHttpMethod());
    }

    public ResponseEntity<Object> mock(final MockHandlerContext mockHandlerContext) {

        this.delay();

        // 初始化自定义空间
        final Object space = this.customizeSpaceInterpreter.interpret(mockHandlerContext);
        if (space instanceof Map && ((Map<?, ?>) space).size() > 0) {
            final Map<String, Object> customizeSpace = new HashMap<>();
            for (final Map.Entry<?, ?> entry : ((Map<?, ?>) space).entrySet()) {
                customizeSpace.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            mockHandlerContext.getRequestContext().setCustomizeSpace(customizeSpace);
        }

        // 筛选异步定时任务
        for (final MockTaskInfo mockTaskInfo : this.asyncTaskInfoList) {
            if (mockTaskInfo.support(mockHandlerContext)) {
                // 初始化异步定时任务
                DynamicMockContext.getInstance()
                        .getMockTaskManager()
                        .registerAsyncTask(mockTaskInfo.getMockTask(mockHandlerContext));
            }
        }

        MockResponseInfo.MockResponse mockResponse = null;

        // 筛选Response
        for (final MockResponseInfo mockResponseInfo : this.responseInfoList) {
            if (mockResponseInfo.support(mockHandlerContext)) {
                // -- 初始化Response
                mockResponse = mockResponseInfo.getMockResponse(mockHandlerContext);
            }
        }

        // 筛选同步task
        for (final MockTaskInfo mockTaskInfo : this.syncTaskInfoList) {
            if (mockTaskInfo.support(mockHandlerContext)) {
                // 同步任务仅执行一次
                DynamicMockContext.getInstance()
                        .getMockTaskManager()
                        .registerDisposableSyncTask(mockTaskInfo.getMockTask(mockHandlerContext));
            }
        }

        if (null == mockResponse) {
            return ResponseEntity.ok("No response available!");
        }

        return mockResponse
                .delay()
                .toResponseEntity();
    }

    public MockHandler delay() {
        DelayUtils.delay(this.delayTime, TimeUnit.MILLISECONDS);
        return this;
    }

}