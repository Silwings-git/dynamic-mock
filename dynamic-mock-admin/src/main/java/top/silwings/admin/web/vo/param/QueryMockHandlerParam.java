package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.common.PageParam;
import top.silwings.core.common.Identity;

/**
 * @ClassName QueryMockHandlerParam
 * @Description 分页查询Mock处理器参数
 * @Author Silwings
 * @Date 2022/11/21 22:58
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "分页查询Mock处理器参数")
public class QueryMockHandlerParam extends PageParam {

    @ApiModelProperty(value = "项目id", required = true, example = "1")
    private Identity projectId;

    @ApiModelProperty(value = "处理器名称", example = "ERP")
    private String name;

    @ApiModelProperty(value = "处理器支持的请求方式", example = "GET")
    private String httpMethod;

    @ApiModelProperty(value = "处理器支持的uri", example = "/test")
    private String requestUri;

    @ApiModelProperty(value = "处理器标签", example = "erp")
    private String label;

}