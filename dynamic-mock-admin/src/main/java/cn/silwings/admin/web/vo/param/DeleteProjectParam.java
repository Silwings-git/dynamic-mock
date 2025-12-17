package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.core.common.Identity;
import cn.silwings.core.utils.CheckUtils;

/**
 * @ClassName DeleteProjectParam
 * @Description 删除项目参数
 * @Author Silwings
 * @Date 2022/11/21 21:48
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "删除项目参数")
public class DeleteProjectParam {

    @ApiModelProperty(value = "项目id", required = true, example = "1")
    private Identity projectId;

    public void validate() {
        CheckUtils.isNotNull(this.projectId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "projectId"));
    }
}