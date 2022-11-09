package top.silwings.core.handler.definition;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MockHandlerDefinition
 * @Description MockHandler定义信息
 * @Author Silwings
 * @Date 2022/11/9 23:22
 * @Since
 **/
@Getter
@Setter
public class MockHandlerDefinition {

    /**
     * 唯一标识符
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 支持的请求方式
     */
    private List<String> httpMethods;

    /**
     * 支持的请求地址
     */
    private String requestUrl;

    /**
     * 自定义标签
     */
    private String label;

    /**
     * 延迟执行时间
     */
    private int delayTime;

    /**
     * 自定义空间
     */
    private Map<String, Object> customizeSpace;

    /**
     * 响应信息集
     */
    private List<ResponseInfoDefinition> responses;

    /**
     * 任务集
     */
    private List<ResponseInfoDefinition> tasks;

}