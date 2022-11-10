package top.silwings.core.handler.definition;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName MockHandlerHttpTaskDefinition
 * @Description Http任务定义
 * @Author Silwings
 * @Date 2022/11/9 23:24
 * @Since
 **/
@Getter
@Setter
public class HttpRequestInfoDefinition {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 支持条件
     */
    private List<String> support;

    /**
     * 延迟执行时间
     */
    private int delayTime;

    /**
     * 是否异步执行
     */
    private boolean async;

    /**
     * 执行次数
     */
    private int numberOfExecute;

    /**
     * 请求信息
     */
    private RequestDefinition request;

}