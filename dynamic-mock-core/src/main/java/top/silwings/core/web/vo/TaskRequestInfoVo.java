package top.silwings.core.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @ClassName TaskRequestInfo
 * @Description 任务请求信息
 * @Author Silwings
 * @Date 2022/11/13 23:59
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "Task请求信息")
public class TaskRequestInfoVo {

    @ApiModelProperty(value = "请求地址", required = true, example = "http://localhost:8080/misaka/mikoto")
    private String requestUrl;

    @ApiModelProperty(value = "请求方式", required = true, example = "GET")
    private String httpMethod;

    @ApiModelProperty(value = "请求头", required = true, example = "GET")
    private Map<String, List<String>> headers;

    @ApiModelProperty(value = "请求体,支持json和单字符串,对应请求方式下必填")
    private String body;

    @ApiModelProperty(value = "路径参数", required = true)
    private Map<String, List<String>> uriVariables;

}