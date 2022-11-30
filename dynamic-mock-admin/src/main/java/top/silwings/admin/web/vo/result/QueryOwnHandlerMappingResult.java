package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import top.silwings.core.common.Identity;

import java.util.List;
import java.util.Map;

/**
 * @ClassName QueryOwnHandlerMappingResult
 * @Description
 * @Author Silwings
 * @Date 2022/11/30 12:00
 * @Since
 **/
@Getter
@Setter
@Builder
public class QueryOwnHandlerMappingResult {

    @ApiModelProperty(value = "项目处理器映射")
    private Map<Identity, List<OwnHandlerInfoResult>> projectHandlerMap;

}