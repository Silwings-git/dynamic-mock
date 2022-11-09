package top.silwings.core.handler.definition;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @ClassName ResultResultInfoDefinition
 * @Description 返回值信息
 * @Author Silwings
 * @Date 2022/11/9 23:54
 * @Since
 **/
@Getter
@Setter
public class ResponseDefinition {

    /**
     * 响应头
     */
    private Map<String, String> headers;

    /**
     * 响应体
     */
    private String body;

}