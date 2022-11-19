package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName UserLoginVo
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:24
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "用户登录响应信息")
public class UserLoginResult {

    @ApiModelProperty(value = "用户名", example = "Misaka Mikoto")
    private String username;

    @ApiModelProperty(value = "token", example = "123456789")
    private String token;

}