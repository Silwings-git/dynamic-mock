package cn.silwings.admin.web.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cn.silwings.admin.auth.WebContextHolder;
import cn.silwings.admin.common.Result;
import cn.silwings.admin.common.WebResult;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.admin.exceptions.UserNotLoggedOnException;
import cn.silwings.admin.utils.I18nUtils;
import cn.silwings.core.exceptions.BaseDynamicMockException;
import cn.silwings.core.exceptions.DynamicValueCompileException;

/**
 * @ClassName DynamicMockAdvice
 * @Description DynamicMockException 异常处理
 * @Author Silwings
 * @Date 2022/11/18 23:27
 * @Since
 **/
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = BaseDynamicMockException.class)
    public ResponseEntity<Object> handlingException(final BaseDynamicMockException exception) {
        log.error("BaseDynamicMockException.", exception);

        if (exception instanceof DynamicValueCompileException) {
            // 表达式编译错误需要在admin中响应,需要支持多语言
            return this.handlingException(DynamicMockAdminException.of(ErrorCode.EXPRESSION_INCORRECT, exception.getMessage()));
        }
        return ResponseEntity.ok(Result.fail(exception.getMessage()));
    }

    @ExceptionHandler(value = DynamicMockAdminException.class)
    public ResponseEntity<Object> handlingException(final DynamicMockAdminException exception) {
        log.error("DynamicMockAdminException.", exception);

        final String value = I18nUtils.getValue(WebContextHolder.getLanguage(), exception.getErrorCode().getCode(), exception.getArgs());

        if (exception instanceof UserNotLoggedOnException) {
            return ResponseEntity.ok(new Result<>(WebResult.UNAUTHORIZED, value, null));
        }

        return ResponseEntity.ok(Result.fail(value));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handlingException(final Exception exception) {
        log.error("unknown error.", exception);
        return this.handlingException(DynamicMockAdminException.from(ErrorCode.UNKNOWN_ERROR));
    }

}