package cn.silwings.admin.repository.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName MockHandlerTaskPo
 * @Description Mock handler
 * @Author Silwings
 * @Date 2023/8/7 14:01
 * @Since 0.1.1
 **/
@Getter
@Setter
@Table(name = "dm_mock_handler_task")
public class MockHandlerTaskPo {

    public static final String C_TASK_ID = "taskId";
    public static final String C_HANDLER_ID = "handlerId";

    /**
     * 任务id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer taskId;

    /**
     * mock处理器id
     */
    @Column(name = "handler_id")
    private Integer handlerId;

    /**
     * 任务名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 启用状态
     *
     * @see cn.silwings.core.common.EnableStatus
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

    /**
     * 是否异步执行
     */
    @Column(name = "async")
    private Boolean async;

    /**
     * cron
     */
    @Column(name = "cron")
    private String cron;

    /**
     * 执行次数
     */
    @Column(name = "number_of_execute")
    private Integer numberOfExecute;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;

}