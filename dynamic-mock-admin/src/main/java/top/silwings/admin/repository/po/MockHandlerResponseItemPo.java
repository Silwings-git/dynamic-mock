package top.silwings.admin.repository.po;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName MockHandlerResponseResponsePo
 * @Description
 * @Author Silwings
 * @Date 2023/8/8 10:48
 * @Since
 **/
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "dm_mock_handler_response_item")
public class MockHandlerResponseItemPo {

    public static final String C_HANDLER_ID = "handlerId";

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer responseItemId;

    /**
     * 唯一标识
     */
    @Column(name = "response_id")
    private Integer responseId;

    /**
     * 处理器ID
     */
    @Column(name = "handler_id")
    private Integer handlerId;

    /**
     * 响应状态码
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 响应头
     */
    @Column(name = "headers")
    private String headers;

    /**
     * 响应体
     */
    @Column(name = "body")
    private String body;

}