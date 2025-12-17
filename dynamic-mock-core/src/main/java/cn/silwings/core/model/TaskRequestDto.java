package cn.silwings.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName RequestInfoDefinition
 * @Description 请求信息定义
 * @Author Silwings
 * @Date 2022/11/9 23:27
 * @Since
 **/
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求方式
     */
    private HttpMethod httpMethod;

    /**
     * 请求头
     */
    private Map<String, List<String>> headers;

    /**
     * 请求体
     */
    private Object body;

    /**
     * 请求表单参数
     */
    private Map<String, List<String>> uriVariables;

}