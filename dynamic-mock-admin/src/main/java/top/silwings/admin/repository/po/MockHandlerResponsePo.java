package top.silwings.admin.repository.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName MockHandlerResponsePo
 * @Description
 * @Author Silwings
 * @Date 2023/8/8 10:43
 * @Since
 **/
@Getter
@Setter
@Table(name = "dm_mock_handler_response")
public class MockHandlerResponsePo {

    public static final String C_HANDLER_ID = "handlerId";

    /**
     * 唯一标识
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer responseId;

    /**
     * 处理器ID
     */
    @Column(name = "handler_id")
    private Integer handlerId;

    /**
     * 响应名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 延迟时间
     */
    @Column(name = "delay_time")
    private Integer delayTime;

}