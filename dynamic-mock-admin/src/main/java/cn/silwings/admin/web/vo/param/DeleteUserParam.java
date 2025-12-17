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
 * @ClassName DeleteUserParam
 * @Description 删除用户参数
 * @Author Silwings
 * @Date 2022/11/25 22:17
 * @Since
 **/
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