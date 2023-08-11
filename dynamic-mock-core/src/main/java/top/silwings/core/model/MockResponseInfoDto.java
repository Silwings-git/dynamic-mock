package top.silwings.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;

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
@AllArgsConstructor
@NoArgsConstructor
public class MockResponseInfoDto {

    /**
     * 响应id
     */
    private Identity responseId;

    /**
     * 返回值名称
     */
    private String name;

    /**
     * 启用状态
     */
    private EnableStatus enableStatus;

    /**
     * 支持条件
     */
    private List<String> support;

    /**
     * 检查信息
     */
    private CheckInfoDto checkInfo;

    /**
     * 延迟响应时间
     */
    private Integer delayTime;

    /**
     * 请求信息
     */
    private MockResponseDto response;

}