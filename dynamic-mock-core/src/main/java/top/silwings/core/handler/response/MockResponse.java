package top.silwings.core.handler.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.DelayUtils;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName MockResponse
 * @Description
 * @Author Silwings
 * @Date 2023/5/16 22:51
 * @Since
 **/
@Getter
@Builder
public class MockResponse {
    private int delayTime;
    private int status;
    private HttpHeaders headers;
    private Object body;

    public ResponseEntity<Object> toResponseEntity() {
        return new ResponseEntity<>(this.body, this.headers, this.status);
    }

    public MockResponse delay(final boolean delay) {
        if (delay) {
            DelayUtils.delay(ConvertUtils.getNoNullOrDefault(this.delayTime, 0), TimeUnit.MILLISECONDS);
        }
        return this;
    }
}