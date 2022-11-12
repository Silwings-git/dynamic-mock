package top.silwings.core.handler.response;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import top.silwings.core.exceptions.DynamicMockException;
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
public class MockResponseInfo {

    private final String name;

    private final List<String> supportList;

    // TODO_Silwings: 2022/11/12 初始化解释器
    private final List<NodeInterpreter> supportInterpreterList;

    private final int delayTime;

    private final MockResponse mockResponse;

    private final NodeInterpreter responseInterpreter;

    public boolean support(final Context context) {

        if (CollectionUtils.isEmpty(this.supportList)) {
            return true;
        }

        for (final NodeInterpreter interpreter : this.supportInterpreterList) {
            if (!Boolean.TRUE.equals(interpreter.interpret(context))) {
                return false;
            }
        }

        return true;
    }

    public MockResponse getMockResponse(final Context context) {

        final Object interpret = this.responseInterpreter.interpret(context);

        if (!(interpret instanceof Map)) {
            throw new DynamicMockException();
        }

        final Map<?, ?> map = (Map<?, ?>) interpret;

        // TODO_Silwings: 2022/11/12 待优化

        return MockResponse.builder()
                .delayTime(this.delayTime)
                .status(Integer.parseInt(String.valueOf(map.get("status"))))
                .headers(this.buildHeader(map.get("headers")))
                .body(map.get("body"))
                .build();
    }

    private HttpHeaders buildHeader(final Object headers) {

        final HttpHeaders httpHeaders = new HttpHeaders();

        if (headers instanceof Map) {

            for (final Map.Entry<?, ?> entry : ((Map<?, ?>) headers).entrySet()) {

                final String headerName = String.valueOf(entry.getKey());
                final Object entryValue = entry.getValue();

                if (entryValue instanceof List) {

                    ((List<?>) entryValue).forEach(headerValue -> httpHeaders.add(headerName, String.valueOf(headerValue)));
                } else {

                    httpHeaders.add(headerName, String.valueOf(entryValue));
                }
            }
        }

        return httpHeaders;
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