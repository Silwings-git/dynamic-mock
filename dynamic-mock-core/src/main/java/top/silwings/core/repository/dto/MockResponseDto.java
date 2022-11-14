package top.silwings.core.repository.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ResultResultInfoDefinition
 * @Description 返回值信息
 * @Author Silwings
 * @Date 2022/11/9 23:54
 * @Since
 **/
@Getter
@Builder
public class MockResponseDto {

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 响应头
     */
    private Map<String, List<String>> headers;

    /**
     * 响应体
     */
    private Object body;

}