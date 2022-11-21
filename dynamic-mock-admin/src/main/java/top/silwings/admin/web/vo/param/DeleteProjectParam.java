package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.core.utils.CheckUtils;

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
    private String projectId;

    public void validate() {
        CheckUtils.isNotBlank(this.projectId, () -> DynamicMockAdminException.from("Project name cannot be empty."));
    }
}