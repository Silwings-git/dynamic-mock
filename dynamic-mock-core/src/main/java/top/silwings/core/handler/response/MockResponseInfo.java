package top.silwings.core.handler.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import top.silwings.core.converter.HttpHeaderConverter;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.handler.AbstractMockSupport;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.tree.NodeInterpreter;
import top.silwings.core.utils.DelayUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName MockResponse
 * @Description Mock响应
 * @Author Silwings
 * @Date 2022/11/10 22:17
 * @Since
 **/
@Builder
public class MockResponseInfo extends AbstractMockSupport {

    private final String name;

    private final List<NodeInterpreter> supportInterpreterList;

    private final int delayTime;

    private final NodeInterpreter responseInterpreter;

    @Override
    protected List<NodeInterpreter> getSupportInterpreterList() {
        return this.supportInterpreterList;
    }

    public MockResponse getMockResponse(final Context context) {

        final Object interpret = this.responseInterpreter.interpret(context);

        if (!(interpret instanceof Map)) {
            throw new DynamicMockException();
        }

        final Map<?, ?> map = (Map<?, ?>) interpret;

        return MockResponse.builder()
                .delayTime(this.delayTime)
                .status(Integer.parseInt(String.valueOf(map.get("status"))))
                .headers(HttpHeaderConverter.from(map.get("headers")))
                .body(map.get("body"))
                .build();
    }

    @Getter
    @Builder
    public static class MockResponse {
        private final int delayTime;
        private final int status;
        private final HttpHeaders headers;
        private final Object body;

        public ResponseEntity<Object> toResponseEntity() {
            return new ResponseEntity<>(this.body, this.headers, this.status);
        }

        public MockResponse delay() {
            DelayUtils.delay(this.delayTime, TimeUnit.MILLISECONDS);
            return this;
        }
    }


}