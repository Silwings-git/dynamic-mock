package top.silwings.admin.repository.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName MockTaskLogPo
 * @Description 任务日志
 * @Author Silwings
 * @Date 2022/11/23 23:11
 * @Since
 **/
@Getter
@Setter
@Table(name = "dm_mock_task_log")
public class MockTaskLogPo {

    public static final String C_LOG_ID = "logId";
    public static final String C_HANDLER_ID = "handlerId";
    public static final String C_NAME = "name";

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer logId;

    @Column(name = "task_code")
    private String taskCode;

    @Column(name = "handler_id")
    private Integer handlerId;

    @Column(name = "name")
    private String name;

    @Column(name = "registration_time")
    private Date registrationTime;

    @Column(name = "request_info")
    private String requestInfo;

    @Column(name = "response_info")
    private String responseInfo;

    @Column(name = "request_time")
    private Date requestTime;

    @Column(name = "timing")
    private Long timing;

    @Column(name = "create_time")
    private Date createTime;

}