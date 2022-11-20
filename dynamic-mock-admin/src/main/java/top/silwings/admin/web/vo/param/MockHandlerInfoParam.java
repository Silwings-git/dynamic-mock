package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MockHandlerInfoVo
 * @Description 模拟处理器信息
 * @Author Silwings
 * @Date 2022/11/14 0:15
 * @Since
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "模拟处理器信息")
public class MockHandlerInfoParam {

    @ApiModelProperty(value = "项目id", required = true, example = "P1")
    private String projectId;

    @ApiModelProperty(value = "处理器id", required = true, example = "H1")
    private String handlerId;

    @ApiModelProperty(value = "处理器名称", required = true, example = "获取用户信息")
    private String name;

    @ApiModelProperty(value = "支持的请求方式", required = true)
    private List<String> httpMethods;

    @ApiModelProperty(value = "支持的请求URI", required = true, example = "/findUser")
    private String requestUri;

    @ApiModelProperty(value = "处理器标签", example = "User Admin")
    private String label;

    @ApiModelProperty(value = "延迟处理时间", required = true, example = "0")
    private Integer delayTime;

    @ApiModelProperty(value = "自定义参数空间")
    private Map<String, ?> customizeSpace;

    @ApiModelProperty(value = "响应信息集")
    private List<MockResponseInfoParam> responses;

    @ApiModelProperty(value = "Task信息集")
    private List<TaskInfoParam> tasks;

}