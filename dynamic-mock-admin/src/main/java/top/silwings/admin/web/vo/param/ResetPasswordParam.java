package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName ResetPasswordParam
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 20:19
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "重置用户密码")
public class ResetPasswordParam {

    @ApiModelProperty(value = "要重置密码的账号", required = true, example = "Misaka Mikoto")
    private String userAccount;

}