package top.silwings.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;

import java.util.Date;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MockHandlerDto {

    /**
     * 项目id
     */
    private Identity projectId;

    /**
     * 处理器id
     */
    private Identity handlerId;

    /**
     * 启用状态.ture-启用,false-停用
     */
    private EnableStatus enableStatus;

    /**
     * 名称
     */
    private String name;

    /**
     * 支持的请求方式
     */
    private List<HttpMethod> httpMethods;

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
    private int delayTime;

    /**
     * 自定义空间
     */
    private Map<String, ?> customizeSpace;

    /**
     * 响应信息集
     */
    private List<MockResponseInfoDto> responses;

    /**
     * 任务集
     */
    private List<TaskInfoDto> tasks;

    private Date updateTime;

}