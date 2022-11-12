package top.silwings.core.handler;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import top.silwings.core.handler.response.MockResponseInfo;
import top.silwings.core.handler.task.MockTaskInfo;
import top.silwings.core.handler.task.TaskQueue;
import top.silwings.core.handler.tree.NodeInterpreter;
import top.silwings.core.utils.PathMacther;

import java.util.List;
import java.util.Map;

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
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 支持的请求方式
     */
    private List<HttpMethod> httpMethodList;

    /**
     * 支持的请求地址
     */
    private String requestUri;

    /**
     * 延迟执行时间
     */
    private int delayTime;

    /**
     * 自定义空间
     */
    private NodeInterpreter customizeSpaceInterpreter;

    private List<MockResponseInfo> responseInfoList;

    private List<MockTaskInfo> syncTaskInfoList;

    private List<MockTaskInfo> asyncTaskInfoList;

    public boolean support(final RequestInfo requestInfo) {
        return PathMacther.match(this.getRequestUri(), requestInfo.getRequestUri()) && this.httpMethodList.contains(requestInfo.getHttpMethod());
    }

    public ResponseEntity<Object> mock(final Context context) {

        // 初始化自定义空间
        final Object customizeSpace = this.customizeSpaceInterpreter.interpret(context);
        if (customizeSpace instanceof Map) {
            context.getHandlerContext().setCustomizeSpace((Map<?, ?>) customizeSpace);
        }

        // 筛选异步定时任务
        for (final MockTaskInfo mockTask : this.asyncTaskInfoList) {
            if (mockTask.support(context)) {
                // -- 初始化异步定时任务
                // TODO_Silwings: 2022/11/12 初始化异步定时任务
                new TaskQueue().registerAsyncTask(mockTask);
            }
        }

        MockResponseInfo.MockResponse mockResponse = null;

        // 筛选Response
        for (final MockResponseInfo mockResponseInfo : this.responseInfoList) {
            if (mockResponseInfo.support(context)) {
                // -- 初始化Response
                mockResponse = mockResponseInfo.getMockResponse(context);
            }
        }

        // 筛选同步task
        for (final MockTaskInfo mockTaskInfo : this.syncTaskInfoList) {
            if (mockTaskInfo.support(context)) {
                // -- 初始化同步定时任务
                // TODO_Silwings: 2022/11/12 初始化同步定时任务
                new TaskQueue().registerAsyncTask(mockTaskInfo);
            }
        }

        if (null == mockResponse) {
            return ResponseEntity.ok().build();
        }

        return mockResponse
                .delay()
                .toResponseEntity();
    }

}