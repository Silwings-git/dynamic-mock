package top.silwings.core.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;

/**
 * @ClassName ErrorResponseInfo
 * @Description
 * @Author Silwings
 * @Date 2023/8/11 11:52
 * @Since
 **/
@Getter
@Setter
public class ErrorResponseInfoDto {

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

}