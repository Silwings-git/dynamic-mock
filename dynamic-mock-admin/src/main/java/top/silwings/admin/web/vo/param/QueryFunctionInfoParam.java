package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.silwings.core.handler.tree.dynamic.function.FunctionReturnType;

/**
 * @ClassName QueryFunctionInfoParam
 * @Description 查询函数信息参数
 * @Author Silwings
 * @Date 2023/1/8 22:41
 * @Since
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "查询函数信息参数")
public class QueryFunctionInfoParam {

    @ApiModelProperty(value = "函数响应类型")
    private FunctionReturnType functionReturnType;

}