package cn.silwings.admin.web.vo.param;

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
 * @ClassName MockResponseVo
 * @Description 模拟响应内容
 * @Author Silwings
 * @Date 2022/11/14 0:12
 * @Since
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "模拟响应内容")
public class MockResponseParam {

    @ApiModelProperty(value = "响应状态", required = true, example = "200")
    private Integer status;

    @ApiModelProperty(value = "响应头", required = true)
    private Map<String, List<String>> headers;

    @ApiModelProperty(value = "响应体,支持json和单字符串,对应请求方式下必填")
    private Object body;

}