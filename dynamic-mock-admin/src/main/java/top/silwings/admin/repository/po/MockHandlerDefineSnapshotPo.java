package top.silwings.admin.repository.po;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "dm_mock_handler_define_snapshot")
public class MockHandlerDefineSnapshotPo {

    public static final String C_SNAPSHOT_ID = "snapshotId";
    public static final String C_HANDLER_ID = "handlerId";
    public static final String C_SNAPSHOT_VERSION = "snapshotVersion";

    /**
     * 快照id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer snapshotId;

    /**
     * 处理器ID
     */
    @Column(name = "handler_id")
    private Integer handlerId;

    /**
     * 版本号
     */
    @Column(name = "snapshot_version")
    private String snapshotVersion;

    /**
     * 定义信息
     */
    @Column(name = "snapshot_data")
    private byte[] snapshotData;

    /**
     * 创建人
     */
    @Column(name = "create_user")
    private String createUser;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

}
