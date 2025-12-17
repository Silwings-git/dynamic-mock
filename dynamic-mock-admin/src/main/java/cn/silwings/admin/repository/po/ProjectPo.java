package cn.silwings.admin.repository.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName ProjectPo
 * @Description 项目
 * @Author Silwings
 * @Date 2022/11/19 19:35
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "dm_project")
public class ProjectPo {

    public static final String C_PROJECT_ID = "projectId";
    public static final String C_PROJECT_NAME = "projectName";

    /**
     * 项目id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer projectId;

    /**
     * 项目名称
     */
    @Column(name = "project_name")
    private String projectName;

    /**
     * 基础uri,允许为空
     */
    @Column(name = "base_uri")
    private String baseUri;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;


}