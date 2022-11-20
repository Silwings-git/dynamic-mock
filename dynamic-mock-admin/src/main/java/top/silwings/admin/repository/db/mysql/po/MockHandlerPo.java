package top.silwings.admin.repository.db.mysql.po;

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
    public static final String C_NAME = "name";
    public static final String C_REQUEST_URI = "requestUri";
    public static final String C_LABEL = "label";

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long handlerId;

    @Column(name = "enable_status")
    private Integer enableStatus;

    @Column(name = "name")
    private String name;

    @Column(name = "http_methods")
    private String httpMethods;

    @Column(name = "request_uri")
    private String requestUri;

    @Column(name = "label")
    private String label;

    /**
     * 延迟执行时间
     */
    @Column(name = "delay_time")
    private Integer delayTime;

    @Column(name = "customize_space")
    private String customizeSpace;

    @Column(name = "responses")
    private String responses;

    @Column(name = "tasks")
    private String tasks;

    @Column(name = "author")
    private String author;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}