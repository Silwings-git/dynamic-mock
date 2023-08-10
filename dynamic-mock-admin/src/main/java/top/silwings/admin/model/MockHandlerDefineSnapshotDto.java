package top.silwings.admin.model;

import lombok.Getter;
import lombok.Setter;
import top.silwings.core.model.MockHandlerDto;

import java.util.Date;

/**
 * @ClassName MockHandlerDefineSnapshotDto
 * @Description
 * @Author Silwings
 * @Date 2023/8/9 16:42
 * @Since
 **/
@Getter
@Setter
public class MockHandlerDefineSnapshotDto {

    /**
     * 快照id
     */
    private Integer snapshotId;

    /**
     * 处理器ID
     */
    private Integer handlerId;

    /**
     * 版本号
     */
    private String snapshotVersion;

    /**
     * 定义信息
     */
    private MockHandlerDto mockHandlerDto;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

}