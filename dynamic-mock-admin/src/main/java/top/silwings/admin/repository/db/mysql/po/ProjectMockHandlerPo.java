package top.silwings.admin.repository.db.mysql.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @ClassName ProjectMockHandlerPo
 * @Description
 * @Author Silwings
 * @Date 2022/11/20 15:06
 * @Since
 **/
@Getter
@Setter
@Table(name = "dm_project_mock_handler")
public class ProjectMockHandlerPo {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "handler_id")
    private Long handlerId;

}