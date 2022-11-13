package top.silwings.core.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
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
@ApiModel(description = "模拟处理器信息")
public class MockHandlerInfoVo {

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
    private Map<String, Object> customizeSpace;

    @ApiModelProperty(value = "响应信息集")
    private List<MockResponseInfoVo> responses;

    @ApiModelProperty(value = "Task信息集")
    private List<TaskInfoVo> tasks;

}