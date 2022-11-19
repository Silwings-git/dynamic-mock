package top.silwings.admin.repository.db.mysql.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @ClassName ProjectPo
 * @Description 项目
 * @Author Silwings
 * @Date 2022/11/19 19:35
 * @Since
 **/
@Getter
@Setter
@Table(name = "dm_project")
public class ProjectPo {

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "base_uri")
    private String baseUri;

}