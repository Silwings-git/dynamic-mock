package cn.silwings.core.handler.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import cn.silwings.core.utils.ConvertUtils;
import cn.silwings.core.utils.DelayUtils;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName MockResponse
 * @Description
 * @Author Silwings
 * @Date 2023/5/16 22:51
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonMockResponse implements MockResponse {
    private int delayTime;
    private int status;
    private HttpHeaders headers;
    private Object body;

    public static CommonMockResponse newInstance() {
        return CommonMockResponse.builder()
                .delayTime(0)
                .status(200)
                .headers(new HttpHeaders())
                .body(null)
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<Object> toResponseEntity() {
        return new ResponseEntity<>(this.body, this.headers, this.status);
    }

    @Override
    public CommonMockResponse delay(final boolean delay) {
        if (delay) {
            DelayUtils.delay(ConvertUtils.getNoNullOrDefault(this.delayTime, 0), TimeUnit.MILLISECONDS);
        }
        return this;
    }

    public static class Builder {
        private int delayTime = 0;
        private int status = 200;
        private HttpHeaders headers;
        private Object body;

        public Builder delayTime(final int delayTime) {
            this.delayTime = delayTime;
            return this;
        }

        public Builder status(final int status) {
            this.status = status;
            return this;
        }

        public Builder headers(final HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(final Object body) {
            this.body = body;
            return this;
        }

        public CommonMockResponse build() {
            final CommonMockResponse response = new CommonMockResponse();
            response.delayTime = this.delayTime;
            response.status = status;
            response.headers = ConvertUtils.getNoNullOrDefault(this.headers, HttpHeaders::new);
            response.body = body;
            return response;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

}