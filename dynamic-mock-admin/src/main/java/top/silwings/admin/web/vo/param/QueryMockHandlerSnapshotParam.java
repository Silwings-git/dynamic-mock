package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.common.PageParam;
import top.silwings.core.common.Identity;

/**
 * @ClassName QueryMockHandlerSnapshotParam
 * @Description
 * @Author Silwings
 * @Date 2023/8/9 17:06
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "分页查询Mock处理器定义快照参数")
public class QueryMockHandlerSnapshotParam extends PageParam {

    @ApiModelProperty(value = "处理器id", example = "1")
    private Identity handlerId;

}