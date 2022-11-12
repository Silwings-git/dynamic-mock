package top.silwings.core.repository.definition;

import lombok.Getter;
import lombok.Setter;
import top.silwings.core.utils.ConvertUtils;

import java.util.Collections;
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
    private String requestUri;

    /**
     * 自定义标签
     */
    private String label;

    /**
     * 延迟执行时间
     */
    private Integer delayTime;

    /**
     * 自定义空间
     */
    private Map<String, Object> customizeSpace;

    /**
     * 响应信息集
     */
    private List<MockResponseInfoDefinition> responses;

    /**
     * 任务集
     */
    private List<MockTaskInfoDefinition> tasks;

    public List<String> getHttpMethods() {
        return ConvertUtils.getNoNullOrDefault(this.httpMethods, Collections.emptyList());
    }

    public Integer getDelayTime() {
        return ConvertUtils.getNoNullOrDefault(this.delayTime, 0);
    }

    public Map<String, Object> getCustomizeSpace() {
        return ConvertUtils.getNoNullOrDefault(this.customizeSpace, Collections.emptyMap());
    }

    public List<MockResponseInfoDefinition> getResponses() {
        return ConvertUtils.getNoNullOrDefault(this.responses, Collections.emptyList());

    }

    public List<MockTaskInfoDefinition> getTasks() {
        return ConvertUtils.getNoNullOrDefault(this.tasks, Collections.emptyList());
    }
}