package top.silwings.core.repository.db.mysql.dao;

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
public class MockHandlerUniqueDao {

    public static final String C_HANDLER_ID = "handlerId";

    @Id
    private Long id;

    @Column(name = "handler_id")
    private Long handlerId;

    @Column(name = "request_uri")
    private String requestUri;

    @Column(name = "http_method")
    private String httpMethod;

    public static MockHandlerUniqueDao of(final Long handlerId, final String requestUri, final String httpMethod) {
        final MockHandlerUniqueDao uniqueDao = new MockHandlerUniqueDao();
        uniqueDao.setHandlerId(handlerId);
        uniqueDao.setRequestUri(requestUri);
        uniqueDao.setHttpMethod(httpMethod);
        return uniqueDao;
    }

}