package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;

/**
 * @ClassName FunctionInfo
 * @Description 函数信息
 * @Author Silwings
 * @Date 2022/11/25 20:43
 * @Since
 **/
@Getter
@Builder
@ApiModel(description = "函数信息")
public class FunctionInfoResult {

    /**
     * 函数名
     */
    @ApiModelProperty(value = "函数名", example = "Search")
    private String functionName;

    /**
     * 最小参数数量
     */
    @ApiModelProperty(value = "最小参数数量", example = "1")
    private int minArgsNumber;

    /**
     * 最大参数数量
     */
    @ApiModelProperty(value = "最大参数数量", example = "2")
    private int maxArgsNumber;

    @ApiModelProperty(value = "函数返回值类型", example = "2")
    private FunctionReturnType functionReturnType;

    public static FunctionInfoResult from(final FunctionInfo functionInfo) {
        return FunctionInfoResult.builder()
                .functionName(functionInfo.getFunctionName())
                .minArgsNumber(functionInfo.getMinArgsNumber())
                .maxArgsNumber(functionInfo.getMaxArgsNumber())
                .functionReturnType(functionInfo.getFunctionReturnType())
                .build();
    }

}