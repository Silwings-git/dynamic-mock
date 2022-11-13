package top.silwings.core.web;

import org.springframework.http.ResponseEntity;
import org.springframework.util.IdGenerator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.silwings.core.handler.Context;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.handler.task.MockTaskManager;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName MockHandlerController
 * @Description MockHandler处理点
 * @Author Silwings
 * @Date 2022/11/10 21:27
 * @Since
 **/
@RestControllerAdvice
public class MockHandlerPoint {

    private final MockHandlerManager mockHandlerManager;

    private final MockTaskManager mockTaskManager;

    private final IdGenerator idGenerator;

    public MockHandlerPoint(final MockHandlerManager mockHandlerManager, final MockTaskManager mockTaskManager, final IdGenerator idGenerator) {
        this.mockHandlerManager = mockHandlerManager;
        this.mockTaskManager = mockTaskManager;
        this.idGenerator = idGenerator;

    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<Object> executeMock(final HttpServletRequest request) {

        return this.mockHandlerManager.mock(Context.from(request, this.mockTaskManager, this.idGenerator));
    }

}