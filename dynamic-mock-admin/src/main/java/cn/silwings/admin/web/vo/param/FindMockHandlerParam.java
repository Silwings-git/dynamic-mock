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
 * @ClassName FindMockHandlerParam
 * @Description 查询Mock处理器参数
 * @Author Silwings
 * @Date 2022/11/21 22:54
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "查询Mock处理器参数")
public class FindMockHandlerParam {

    @ApiModelProperty(value = "处理器id", required = true, example = "1")
    private Identity handlerId;

    public void validate() {
        CheckUtils.isNotNull(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "handlerId"));
    }

}