package top.silwings.core.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName MockResponseInfoVo
 * @Description 模拟响应信息
 * @Author Silwings
 * @Date 2022/11/14 0:07
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "模拟响应信息")
public class MockResponseInfoVo {

    @ApiModelProperty(value = "名称", required = true, example = "正确响应")
    private String name;

    @ApiModelProperty(value = "支持表达式")
    private List<String> support;

    @ApiModelProperty(value = "延迟响应时间", required = true, example = "0")
    private Integer delayTime;

    @ApiModelProperty(value = "模拟响应内容", required = true)
    private MockResponseVo response;

}