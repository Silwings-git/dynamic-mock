package cn.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @ClassName MockHandlerDefineSnapshotResult
 * @Description
 * @Author Silwings
 * @Date 2023/8/9 16:42
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "Mock handler定义快照")
public class MockHandlerDefineSnapshotResult {

    /**
     * 快照id
     */
    @ApiModelProperty(value = "快照id", example = "1")
    private Integer snapshotId;

    /**
     * 处理器ID
     */
    @ApiModelProperty(value = "处理器ID", example = "1")
    private Integer handlerId;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号", example = "20230101101011000")
    private String snapshotVersion;

    /**
     * 定义信息
     */
    @ApiModelProperty(value = "定义信息")
    private MockHandlerInfoResult mockHandlerInfo;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", example = "1")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", example = "2023-01-01 00:00:00")
    private Date createTime;

}