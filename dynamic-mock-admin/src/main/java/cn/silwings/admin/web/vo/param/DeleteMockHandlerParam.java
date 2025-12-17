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
 * @ClassName DeleteMockHandlerParam
 * @Description 删除Mock处理器参数
 * @Author Silwings
 * @Date 2022/11/21 23:18
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "删除Mock处理器参数")
public class DeleteMockHandlerParam {

    @ApiModelProperty(value = "处理器id", required = true, example = "1")
    private Identity handlerId;

    public void validate() {
        CheckUtils.isNotNull(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "handlerId"));
    }

}