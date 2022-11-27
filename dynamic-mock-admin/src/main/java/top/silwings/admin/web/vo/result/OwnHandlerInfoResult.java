package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.model.HandlerInfoDto;
import top.silwings.core.common.Identity;

/**
 * @ClassName OwnHandlerInfoResult
 * @Description 处理器信息
 * @Author Silwings
 * @Date 2022/11/27 12:04
 * @Since
 **/
@Getter
@Setter
@Builder
@ApiModel(description = "处理器信息")
public class OwnHandlerInfoResult {

    @ApiModelProperty(value = "项目id", example = "111")
    private Identity projectId;

    @ApiModelProperty(value = "处理器id", example = "111")
    private Identity handlerId;

    @ApiModelProperty(value = "处理器名称", example = "111")
    private String name;

    public static OwnHandlerInfoResult from(final HandlerInfoDto dto) {
        return OwnHandlerInfoResult.builder()
                .projectId(dto.getProjectId())
                .handlerId(dto.getHandlerId())
                .name(dto.getName())
                .build();
    }
}