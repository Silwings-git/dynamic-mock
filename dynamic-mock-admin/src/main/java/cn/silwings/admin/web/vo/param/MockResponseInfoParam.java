package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import cn.silwings.core.common.Identity;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "模拟响应信息")
public class MockResponseInfoParam {

    @ApiModelProperty(value = "响应id", example = "1")
    private Identity responseId;

    @ApiModelProperty(value = "名称", required = true, example = "正确响应")
    private String name;

    @ApiModelProperty(value = "启用状态", example = "1")
    private Integer enableStatus;

    @ApiModelProperty(value = "支持表达式")
    private List<String> support;

    @ApiModelProperty(value = "延迟响应时间", required = true, example = "0")
    private Integer delayTime;

    @ApiModelProperty(value = "校验信息")
    private CheckInfoParam checkInfo;

    @ApiModelProperty(value = "模拟响应内容", required = true)
    private MockResponseParam response;

}