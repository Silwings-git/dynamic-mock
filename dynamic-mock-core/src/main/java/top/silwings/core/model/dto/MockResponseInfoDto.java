package top.silwings.core.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @ClassName MockHandlerResultDefinition
 * @Description MockHandler返回值定义
 * @Author Silwings
 * @Date 2022/11/9 23:50
 * @Since
 **/
@Getter
@Builder
public class MockResponseInfoDto {

    /**
     * 返回值名称
     */
    private String name;

    /**
     * 支持条件
     */
    private List<String> support;

    /**
     * 延迟响应时间
     */
    private Integer delayTime;

    /**
     * 请求信息
     */
    private MockResponseDto response;

}