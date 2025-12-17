package cn.silwings.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import cn.silwings.core.config.MockHandlerHolder;
import cn.silwings.core.handler.context.MockHandlerContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName MockHandlerController
 * @Description MockHandler处理点
 * @Author Silwings
 * @Date 2022/11/10 21:27
 * @Since
 **/
@Slf4j
@Component
public class MockEndPoint {

    private final MockHandlerManager mockHandlerManager;

    public MockEndPoint(final MockHandlerManager mockHandlerManager) {
        this.mockHandlerManager = mockHandlerManager;
    }

    public ResponseEntity<?> executeMock(final HttpServletRequest request) {
        try {
            return this.mockHandlerManager.mock(MockHandlerContext.from(request));
        } finally {
            MockHandlerHolder.remove();
        }
    }

}