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

import java.util.Date;

/**
 * @ClassName MockTaskLogResult
 * @Description 分页查询任务日志返回值
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
public class QueryMockTaskLogResult {

    @ApiModelProperty(value = "日志id", example = "1")
    private Identity logId;

    @ApiModelProperty(value = "任务编码", example = "111")
    private String taskCode;

    @ApiModelProperty(value = "Mock 处理器id", example = "222")
    private Identity handlerId;

    @ApiModelProperty(value = "任务名", example = "erp")
    private String name;

    @ApiModelProperty(value = "注册时间")
    private Date registrationTime;

    @ApiModelProperty(value = "请求发起时间")
    private Date requestTime;

    @ApiModelProperty(value = "耗时(ms)", example = "100")
    private Long timing;

    public static QueryMockTaskLogResult from(final MockTaskLogDto mockTaskLog) {

        return QueryMockTaskLogResult.builder()
                .logId(mockTaskLog.getLogId())
                .taskCode(mockTaskLog.getTaskCode())
                .handlerId(mockTaskLog.getHandlerId())
                .name(mockTaskLog.getName())
                .registrationTime(mockTaskLog.getRegistrationTime())
                .requestTime(mockTaskLog.getRequestTime())
                .timing(mockTaskLog.getTiming())
                .build();
    }
}