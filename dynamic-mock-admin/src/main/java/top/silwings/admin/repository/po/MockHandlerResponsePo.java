package top.silwings.admin.repository.po;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@Table(name = "dm_mock_handler_response")
public class MockHandlerResponsePo {

    public static final String C_HANDLER_ID = "handlerId";
    public static final String C_RESPONSE_ID = "responseId";

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
     * 启用状态
     *
     * @see top.silwings.core.common.EnableStatus
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

    /**
     * 延迟时间
     */
    @Column(name = "delay_time")
    private Integer delayTime;

    /**
     * 处理器参数校验信息
     */
    @Column(name = "check_info_json")
    private String checkInfoJson;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;

}