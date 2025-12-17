package cn.silwings.core.handler.check;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import cn.silwings.core.handler.response.MockResponse;

/**
 * @ClassName ErrorResponseInfo
 * @Description
 * @Author Silwings
 * @Date 2023/8/11 11:52
 * @Since
 **/
@Getter
@Setter
@Builder
public class ErrorResponseInfo implements MockResponse {

    /**
     * http状态码
     */
    private int status;

    /**
     * 响应头信息
     */
    private HttpHeaders headers;

    /**
     * 响应体
     */
    private Object body;

    @Override
    public ResponseEntity<?> toResponseEntity() {
        return new ResponseEntity<>(this.body, this.headers, this.status);
    }

    @Override
    public MockResponse delay(final boolean delay) {
        return this;
    }
}