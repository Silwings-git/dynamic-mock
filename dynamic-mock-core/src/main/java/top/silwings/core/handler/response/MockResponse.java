package top.silwings.core.handler.response;

import org.springframework.http.ResponseEntity;

/**
 * @ClassName MockResponse
 * @Description
 * @Author Silwings
 * @Date 2023/7/19 01:16
 * @Since
 **/
public interface MockResponse {
    ResponseEntity<?> toResponseEntity();

    MockResponse delay(boolean delay);
}