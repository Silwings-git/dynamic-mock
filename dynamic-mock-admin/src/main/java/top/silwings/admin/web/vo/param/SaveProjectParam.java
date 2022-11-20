package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.core.utils.CheckUtils;

/**
 * @ClassName SaveProjectParam
 * @Description 保存项目参数
 * @Author Silwings
 * @Date 2022/11/20 13:43
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "保存项目参数")
public class SaveProjectParam {

    @ApiModelProperty(value = "项目id", example = "12")
    private String projectId;

    @ApiModelProperty(value = "项目名称", required = true, example = "ERP")
    private String projectName;

    @ApiModelProperty(value = "基础路径", example = "/erp")
    private String baseUri;

    public void validate() {
        CheckUtils.isNotBlank(this.projectName, () -> DynamicMockAdminException.from("The project name cannot be empty."));
    }
}