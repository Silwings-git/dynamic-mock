package top.silwings.core.repository.db.mysql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

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
public class MockHandlerDao {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "http_methods")
    private String httpMethods;

    @Column(name = "http_methods")
    private String requestUri;

    @Column(name = "label")
    private String label;

    @Column(name = "customize_space")
    private String customizeSpace;

    @Column(name = "responses")
    private String responses;

    @Column(name = "tasks")
    private String tasks;

}