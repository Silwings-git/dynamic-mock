package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.common.enums.ProjectUserType;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.core.utils.CheckUtils;

/**
 * @ClassName AssociateUserParam
 * @Description 关联用户参数
 * @Author Silwings
 * @Date 2022/11/20 15:47
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "关联用户参数")
public class AssociateUserParam {

    @ApiModelProperty(value = "项目id", example = "1")
    private String projectId;

    @ApiModelProperty(value = "用户id", example = "2")
    private String userId;

    @ApiModelProperty(value = "关联类型", example = "1")
    private Integer type;

    public void validate() {
        CheckUtils.isNotBlank(this.projectId, () -> DynamicMockAdminException.from("The project id cannot be empty."));
        CheckUtils.isNotBlank(this.userId, () -> DynamicMockAdminException.from("The user id cannot be empty."));
        CheckUtils.isNotNull(this.type, () -> DynamicMockAdminException.from("The type cannot be empty."));
        CheckUtils.isNotNull(ProjectUserType.valueOfCode(this.type), () -> DynamicMockAdminException.from("Error in type."));
    }
}