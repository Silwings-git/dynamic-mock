package top.silwings.admin.repository.db.mysql.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @ClassName ProjectUserPo
 * @Description 项目与用户关系
 * @Author Silwings
 * @Date 2022/11/20 14:09
 * @Since
 **/
@Getter
@Setter
@Table(name = "dm_project_user")
public class ProjectUserPo {

    public static final String C_PROJECT_ID = "projectId";
    public static final String C_USER_ID = "userId";
    public static final String C_TYPE = "type";

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "type")
    private Integer type;

}