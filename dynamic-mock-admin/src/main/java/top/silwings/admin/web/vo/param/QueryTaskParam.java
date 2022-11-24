package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.core.utils.CheckUtils;

/**
 * @ClassName QueryTaskParam
 * @Description 查询任务列表参数
 * @Author Silwings
 * @Date 2022/11/23 20:46
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "查询任务列表参数")
public class QueryTaskParam {

    @ApiModelProperty(value = "项目id", required = true, example = "1")
    private String projectId;

    @ApiModelProperty(value = "Mock 处理器Id", required = true, example = "1")
    private String handlerId;

    public void validate() {

        CheckUtils.isInteger(this.projectId, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "projectId"));

        if (StringUtils.isNotBlank(this.handlerId)) {
            CheckUtils.isInteger(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "handlerId"));
        }
    }

}