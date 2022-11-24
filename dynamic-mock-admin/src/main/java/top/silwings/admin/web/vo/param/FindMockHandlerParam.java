package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.core.utils.CheckUtils;

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
    private String handlerId;

    public void validate() {
        CheckUtils.isInteger(this.handlerId, DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "handlerId"));
    }

}