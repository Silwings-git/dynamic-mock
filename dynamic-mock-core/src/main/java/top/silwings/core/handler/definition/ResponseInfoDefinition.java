package top.silwings.core.handler.definition;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName MockHandlerResultDefinition
 * @Description MockHandler返回值定义
 * @Author Silwings
 * @Date 2022/11/9 23:50
 * @Since
 **/
@Getter
@Setter
public class ResponseInfoDefinition {

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
    private int delayTime;

    /**
     * 请求信息
     */
    private ResponseDefinition response;

}