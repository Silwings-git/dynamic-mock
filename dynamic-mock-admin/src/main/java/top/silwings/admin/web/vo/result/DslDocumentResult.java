package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName DslDocumentResult
 * @Description DSL文档结果
 * @Author Silwings
 * @Date 2025/12/17
 **/
@Data
@ApiModel("DSL文档结果")
public class DslDocumentResult {

    @ApiModelProperty("文档章节列表")
    private List<Section> sections;

    @Data
    @ApiModel("文档章节")
    public static class Section {
        @ApiModelProperty("章节标题")
        private String title;

        @ApiModelProperty("章节描述")
        private String description;

        @ApiModelProperty("子章节列表")
        private List<SubSection> subSections;
    }

    @Data
    @ApiModel("子章节")
    public static class SubSection {
        @ApiModelProperty("子章节标题")
        private String title;

        @ApiModelProperty("内容列表")
        private List<String> content;

        @ApiModelProperty("示例列表")
        private List<String> examples;
    }
}
