package top.silwings.core.handler.response;

import lombok.Builder;
import top.silwings.core.converter.HttpHeaderConverter;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.AbstractSupportAble;
import top.silwings.core.handler.MockWorkflowControl;
import top.silwings.core.handler.check.CheckInfo;
import top.silwings.core.handler.check.CheckResult;
import top.silwings.core.handler.context.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionInterpreter;
import top.silwings.core.utils.ConvertUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MockResponse
 * @Description Mock响应
 * @Author Silwings
 * @Date 2022/11/10 22:17
 * @Since
 **/
@Builder
public class MockResponseInfo extends AbstractSupportAble {

    private final String name;

    private final List<ExpressionInterpreter> supportInterpreterList;

    private final int delayTime;

    private final ExpressionInterpreter responseInterpreter;

    private final CheckInfo checkInfo;

    @Override
    protected List<ExpressionInterpreter> getSupportInterpreterList() {
        return this.supportInterpreterList;
    }

    public MockResponse getMockResponse(final MockHandlerContext mockHandlerContext) {

        final Object interpret = this.responseInterpreter.interpret(mockHandlerContext);

        if (!(interpret instanceof Map)) {
            throw new DynamicMockException("Response parsing failed: " + this.name);
        }

        final Map<?, ?> map = (Map<?, ?>) interpret;

        return CommonMockResponse.builder()
                .delayTime(ConvertUtils.getNoNullOrDefault(this.delayTime, 0))
                .status(Integer.parseInt(String.valueOf(map.get("status"))))
                .headers(HttpHeaderConverter.from(map.get("headers")))
                .body(map.get("body"))
                .build();
    }

    public void check(final MockHandlerContext mockHandlerContext, final MockWorkflowControl mockWorkflowControl) {
        final CheckResult checkResult = this.checkInfo.check(mockHandlerContext);
        if (!checkResult.isPassed()) {
            mockWorkflowControl.setInterruptAndReturn(true);
            mockWorkflowControl.setInterruptResult(checkResult.getCheckFailedResponse());
        }
    }
}