package top.silwings.core.handler.definition;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @ClassName RequestInfoDefinition
 * @Description 请求信息定义
 * @Author Silwings
 * @Date 2022/11/9 23:27
 * @Since
 **/
@Getter
@Setter
public class RequestDefinition {

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求方式
     */
    private String httpMethod;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * 请求体
     */
    private Map<String, Object> body;

    /**
     * 请求表单参数
     */
    private Map<String, String> params;

}