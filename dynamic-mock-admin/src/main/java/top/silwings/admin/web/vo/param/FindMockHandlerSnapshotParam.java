package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.core.utils.CheckUtils;

/**
 * @ClassName FindMockHandlerSnapshotParam
 * @Description 查询Mock handler快照参数
 * @Author Silwings
 * @Date 2023/8/9 16:57
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "查询Mock handler快照参数")
public class FindMockHandlerSnapshotParam {

    @ApiModelProperty(value = "版本号", required = true, example = "1")
    private String snapshotVersion;

    public void validate() {
        CheckUtils.isNotBlank(this.snapshotVersion, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "snapshotVersion"));
    }
}