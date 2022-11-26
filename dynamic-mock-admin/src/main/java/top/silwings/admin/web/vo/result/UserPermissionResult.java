package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import top.silwings.core.common.Identity;

/**
 * @ClassName UserPermissionResult
 * @Description 用户权限
 * @Author Silwings
 * @Date 2022/11/26 22:04
 * @Since
 **/
@Getter
@Setter
@Builder
@ApiModel(description = "用户权限")
public class UserPermissionResult {

    @ApiModelProperty(value = "项目id", example = "10")
    private Identity projectId;

    @ApiModelProperty(value = "项目名称", example = "ERP")
    private String projectName;

    @ApiModelProperty(value = "是否拥有", example = "true")
    private boolean own;

}