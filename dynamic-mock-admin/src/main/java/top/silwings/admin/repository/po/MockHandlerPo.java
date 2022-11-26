package top.silwings.admin.repository.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName MockHandlerDao
 * @Description Mock处理器实体类
 * @Author Silwings
 * @Date 2022/11/15 22:29
 * @Since
 **/
@Getter
@Setter
@Table(name = "dm_mock_handler")
public class MockHandlerPo {

    public static final String C_ENABLE_STATUS = "enableStatus";
    public static final String C_HANDLER_ID = "handlerId";
    public static final String C_PROJECT_ID = "projectId";
    public static final String C_NAME = "name";
    public static final String C_REQUEST_URI = "requestUri";
    public static final String C_LABEL = "label";

    /**
     * 唯一标识
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer handlerId;

    /**
     * 所属项目id
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 启用状态.
     * {@link top.silwings.core.common.EnableStatus}
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

    /**
     * 处理器名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 支持的请求方式
     */
    @Column(name = "http_methods")
    private String httpMethods;

    /**
     * 支持的请求URI
     */
    @Column(name = "request_uri")
    private String requestUri;

    /**
     * 自定义标签
     */
    @Column(name = "label")
    private String label;

    /**
     * 延迟执行时间
     */
    @Column(name = "delay_time")
    private Integer delayTime;

    /**
     * 自定义参数空间
     */
    @Column(name = "customize_space")
    private String customizeSpace;

    /**
     * 模拟响应信息
     */
    @Column(name = "responses")
    private String responses;

    /**
     * 任务集
     */
    @Column(name = "tasks")
    private String tasks;

    /**
     * 负责人
     */
    @Column(name = "author")
    private String author;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

}