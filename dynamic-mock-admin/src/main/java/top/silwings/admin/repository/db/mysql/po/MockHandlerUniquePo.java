package top.silwings.admin.repository.db.mysql.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName MockHandlerUniqueDao
 * @Description MockHandler唯一表
 * @Author Silwings
 * @Date 2022/11/15 22:54
 * @Since
 **/
@Getter
@Setter
@Table(name = "dm_mock_handler_unique")
public class MockHandlerUniquePo {

    public static final String C_HANDLER_ID = "handlerId";

    @Column(name = "handler_id")
    private Integer handlerId;

    @Id
    @Column(name = "request_uri")
    private String requestUri;

    @Id
    @Column(name = "http_method")
    private String httpMethod;

    public static MockHandlerUniquePo of(final Integer handlerId, final String requestUri, final String httpMethod) {
        final MockHandlerUniquePo uniqueDao = new MockHandlerUniquePo();
        uniqueDao.setHandlerId(handlerId);
        uniqueDao.setRequestUri(requestUri);
        uniqueDao.setHttpMethod(httpMethod);
        return uniqueDao;
    }

}