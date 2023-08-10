package top.silwings.admin.repository.po;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpMethod;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName TaskRequestPo
 * @Description Task request
 * @Author Silwings
 * @Date 2023/8/7 14:43
 * @Since
 **/
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "dm_mock_handler_task_request")
public class MockHandlerTaskRequestPo {

    public static final String C_HANDLER_ID = "handlerId";

    /**
     * 请求id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer taskRequestId;

    /**
     * mock handler id
     */
    private Integer handlerId;

    /**
     * 任务id
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 请求地址
     */
    @Column(name = "request_url")
    private String requestUrl;

    /**
     * 请求方式
     */
    @Column(name = "http_method")
    private HttpMethod httpMethod;

    /**
     * 请求头
     */
    @Column(name = "headers")
    private String headers;

    /**
     * 请求体
     */
    @Column(name = "body")
    private String body;

    /**
     * 请求表单参数
     */
    @Column(name = "uri_variables")
    private String uriVariables;

}