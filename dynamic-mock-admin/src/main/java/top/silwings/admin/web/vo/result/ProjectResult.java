package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.model.ProjectSummary;

/**
 * @ClassName ProjectResult
 * @Description
 * @Author Silwings
 * @Date 2022/11/20 15:28
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "项目信息")
public class ProjectResult {

    @ApiModelProperty(value = "项目id", example = "1")
    private String projectId;

    @ApiModelProperty(value = "项目名", example = "ERP")
    private String projectName;

    @ApiModelProperty(value = "基础uri", example = "/misaka")
    private String baseUri;

    public static ProjectResult from(final ProjectSummary project) {

        final ProjectResult projectResult = new ProjectResult();
        projectResult.setProjectId(project.getProjectId().stringValue());
        projectResult.setProjectName(project.getProjectName());
        projectResult.setBaseUri(project.getBaseUri());

        return projectResult;
    }
}