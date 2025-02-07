package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockTaskLogDto;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;

import java.util.Date;
import java.util.Map;

/**
 * @ClassName MockTaskLogResult
 * @Description 任务日志返回值
 * @Author Silwings
 * @Date 2022/11/24 22:05
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "任务日志返回值")
public class MockTaskLogResult {

    @ApiModelProperty(value = "日志id", example = "1")
    private Identity logId;

    @ApiModelProperty(value = "任务编码", example = "111")
    private String taskCode;

    @ApiModelProperty(value = "Mock 项目id", example = "222")
    private Identity projectId;

    @ApiModelProperty(value = "Mock 项目名称", example = "222")
    private String projectName;

    @ApiModelProperty(value = "Mock 处理器id", example = "222")
    private Identity handlerId;

    @ApiModelProperty(value = "Mock 处理器名称", example = "222")
    private String handlerName;

    @ApiModelProperty(value = "任务名", example = "erp")
    private String name;

    @ApiModelProperty(value = "请求响应状态码", example = "200")
    private String responseStatus;

    @ApiModelProperty(value = "注册时间")
    private Date registrationTime;

    @ApiModelProperty(value = "请求信息", example = "{\"body\": {\"body\": {\"name\": \"Misaka Mikoto\", \"underAge\": true}, \"headers\": {\"connection\": [\"keep-alive\"], \"content-type\": [\"application/json\"], \"accept-encoding\": [\"gzip, deflate, br\"]}, \"httpMethod\": \"GET\", \"requestUrl\": \"localhost:8888/test\", \"uriVariables\": {}}, \"headers\": {\"connection\": [\"keep-alive\"], \"content-type\": [\"application/json\"], \"accept-encoding\": [\"gzip, deflate, br\"]}, \"httpMethod\": \"GET\", \"requestUrl\": \"localhost:8888/test\", \"uriVariables\": null}")
    private Map<String, ?> requestInfo;

    @ApiModelProperty(value = "响应信息", example = "{\"body\": \"{\\\"message\\\":\\\"御坂美琴\\\"}\"}")
    private Map<String, ?> responseInfo;

    @ApiModelProperty(value = "请求发起时间")
    private Date requestTime;

    @ApiModelProperty(value = "耗时(ms)", example = "100")
    private String timing;

    public static MockTaskLogResult from(final MockTaskLogDto mockTaskLog) {

        return MockTaskLogResult.builder()
                .logId(mockTaskLog.getLogId())
                .taskCode(mockTaskLog.getTaskCode())
                .handlerId(mockTaskLog.getHandlerId())
                .name(mockTaskLog.getName())
                .registrationTime(mockTaskLog.getRegistrationTime())
                .requestInfo(JsonUtils.toMap(mockTaskLog.getRequestInfo(), String.class, Object.class))
                .responseInfo(JsonUtils.toMap(mockTaskLog.getResponseInfo(), String.class, Object.class))
                .requestTime(mockTaskLog.getRequestTime())
                .timing(String.valueOf(ConvertUtils.getNoNullOrDefault(mockTaskLog.getTiming(), "-")))
                .responseStatus(ConvertUtils.getNoBlankOrDefault(mockTaskLog.getResponseInfo(), "-", res -> String.valueOf(ConvertUtils.getNoNullOrDefault(JsonUtils.jsonPathRead(res, "$.httpStatus"), "Not recorded"))))
                .build();
    }
}