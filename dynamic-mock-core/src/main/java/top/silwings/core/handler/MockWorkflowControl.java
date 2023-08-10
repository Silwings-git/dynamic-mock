package top.silwings.core.handler;

import lombok.Getter;
import lombok.Setter;
import top.silwings.core.handler.response.CommonMockResponse;
import top.silwings.core.handler.response.MockResponse;

/**
 * @ClassName Control
 * @Description
 * @Author Silwings
 * @Date 2023/5/16 21:25
 * @Since
 **/
@Getter
@Setter
public class MockWorkflowControl {

    /**
     * 是否执行处理器延迟
     */
    private boolean executeHandlerDelay = true;

    /**
     * 是否执行异步任务
     */
    private boolean executeAsyncTask = true;

    /**
     * 是否执行响应
     */
    private boolean executeResponse = true;

    /**
     * 是否执行同步任务
     */
    private boolean executeSyncTask = true;

    /**
     * 是否执行响应延迟
     * 仅当执行响应时才会执行
     */
    private boolean executeResponseDelay = true;

    /**
     * 是否立即中断并返回
     * 当该变量值为true时,表示直接使用该实例中提供的中断结果数据进行响应,而不继续执行后续的处理器代码
     */
    private boolean interruptAndReturn = false;

    /**
     * 中断结果数据
     * 当handler的执行被中断时,使用该结果进行响应
     */
    private MockResponse interruptResult;

    private MockWorkflowControl() {
    }

    public static MockWorkflowControl build() {
        final MockWorkflowControl control = new MockWorkflowControl();
        control.setInterruptResult(CommonMockResponse.newInstance());
        return control;
    }

}