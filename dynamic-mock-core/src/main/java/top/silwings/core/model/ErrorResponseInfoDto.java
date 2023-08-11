package top.silwings.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ErrorResponseInfo
 * @Description
 * @Author Silwings
 * @Date 2023/8/11 11:52
 * @Since
 **/
@Getter
@Setter
@Accessors(chain = true)
public class ErrorResponseInfoDto {

    /**
     * 错误响应Code
     */
    private String errResCode;

    /**
     * http状态码
     */
    private int status;

    /**
     * 响应头信息
     */
    private Map<String, List<String>> headers;

    /**
     * 响应体
     */
    private Object body;

}