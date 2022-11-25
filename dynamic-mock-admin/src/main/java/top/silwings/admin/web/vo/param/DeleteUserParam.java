package top.silwings.admin.web.vo.param;

/**
 * @ClassName DeleteUserParam
 * @Description 删除用户参数
 * @Author Silwings
 * @Date 2022/11/25 22:17
 * @Since
 **/

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.CheckUtils;

@Getter
@Setter
@ApiModel(description = "删除任务日志参数")
public class DeleteUserParam {

    @ApiModelProperty(value = "用户id", required = true, example = "11")
    private Identity userId;

    public void validate() {
        CheckUtils.isNotNull(this.userId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "userId"));
    }

}