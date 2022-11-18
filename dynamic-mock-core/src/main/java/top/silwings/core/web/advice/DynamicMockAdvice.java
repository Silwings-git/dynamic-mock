package top.silwings.core.web.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.silwings.core.common.Result;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;

/**
 * @ClassName DynamicMockAdvice
 * @Description DynamicMockException 异常处理
 * @Author Silwings
 * @Date 2022/11/18 23:27
 * @Since
 **/
@RestControllerAdvice
public class DynamicMockAdvice {

    @ExceptionHandler(value = DynamicMockException.class)
    public ResponseEntity<Object> executeMock(final DynamicMockException exception) {
        if (exception instanceof DynamicValueCompileException) {
            return ResponseEntity.ok(Result.fail("The expression content is incorrect: " + exception.getMessage()));
        }
        return ResponseEntity.ok(Result.fail(exception.getMessage()));
    }

}