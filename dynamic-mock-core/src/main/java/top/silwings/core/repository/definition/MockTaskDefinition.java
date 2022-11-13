package top.silwings.core.repository.definition;

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
public class MockTaskDefinition {

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
    private Map<String, ?> headers;

    /**
     * 请求体
     */
    private Map<String, ?> body;

    /**
     * 请求表单参数
     */
    private Map<String, ?> uriVariables;

}