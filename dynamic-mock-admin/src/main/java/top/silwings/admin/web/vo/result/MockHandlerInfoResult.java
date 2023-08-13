package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.silwings.admin.web.vo.param.CheckInfoParam;
import top.silwings.admin.web.vo.param.MockHandlerPluginInfoParam;
import top.silwings.admin.web.vo.param.MockResponseInfoParam;
import top.silwings.admin.web.vo.param.SaveTaskInfoParam;
import top.silwings.core.common.Identity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MockHandlerInfoResultVo
 * @Description Mock处理器信息
 * @Author Silwings
 * @Date 2022/11/16 21:14
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "模拟处理器信息")
public class MockHandlerInfoResult {

    @ApiModelProperty(value = "项目id", example = "P1")
    private Identity projectId;

    @ApiModelProperty(value = "项目名称", example = "P1")
    private String projectName;

    @ApiModelProperty(value = "项目基础路径", example = "/basic")
    private String baseUri;

    @ApiModelProperty(value = "处理器id", example = "H1")
    private Identity handlerId;

    @ApiModelProperty(value = "处理器名称", example = "获取用户信息")
    private String name;

    @ApiModelProperty(value = "支持的请求方式")
    private List<String> httpMethods;

    @ApiModelProperty(value = "支持的请求URI", example = "/findUser")
    private String requestUri;

    @ApiModelProperty(value = "启用状态", example = "enable")
    private Integer enableStatus;

    @ApiModelProperty(value = "处理器标签", example = "User Admin")
    private String label;

    @ApiModelProperty(value = "延迟处理时间", example = "0")
    private Integer delayTime;

    @ApiModelProperty(value = "自定义参数空间")
    private Map<String, ?> customizeSpace;

    @ApiModelProperty(value = "参数校验信息")
    private CheckInfoParam checkInfo;

    @ApiModelProperty(value = "响应信息集")
    private List<MockResponseInfoParam> responses;

    @ApiModelProperty(value = "Task信息集")
    private List<SaveTaskInfoParam> tasks;

    @ApiModelProperty(value = "插件信息集")
    private List<MockHandlerPluginInfoParam> pluginInfos;

    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;

}