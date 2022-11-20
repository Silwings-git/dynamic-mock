package top.silwings.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;
import org.springframework.web.client.AsyncRestTemplate;
import top.silwings.core.handler.task.MockTaskManager;

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
public class MockHandlerPoint {

    private final MockHandlerManager mockHandlerManager;

    private final MockTaskManager mockTaskManager;

    private final IdGenerator idGenerator;

    private final JsonNodeParser jsonNodeParser;

    private final AsyncRestTemplate asyncRestTemplate;

    public MockHandlerPoint(final MockHandlerManager mockHandlerManager, final MockTaskManager mockTaskManager, final IdGenerator idGenerator, final JsonNodeParser jsonNodeParser, final AsyncRestTemplate asyncRestTemplate) {
        this.mockHandlerManager = mockHandlerManager;
        this.mockTaskManager = mockTaskManager;
        this.idGenerator = idGenerator;
        this.jsonNodeParser = jsonNodeParser;
        this.asyncRestTemplate = asyncRestTemplate;
    }

    public ResponseEntity<Object> executeMock(final HttpServletRequest request) {
        return this.mockHandlerManager.mock(Context.from(request, this.mockTaskManager, this.idGenerator, this.jsonNodeParser, this.asyncRestTemplate));
    }

}