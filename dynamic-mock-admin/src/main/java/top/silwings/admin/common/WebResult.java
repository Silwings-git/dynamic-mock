package top.silwings.admin.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * @ClassName HttpRes
 * @Description
 * @Author Silwings
 * @Date 2022/11/14 23:58
 * @Since
 **/
@Getter
@ApiModel(description = "WebResult")
public abstract class WebResult {
    public static final String SUCCESS = "200";
    public static final String FAIL = "400";

    public static final String UNAUTHORIZED = "401";

    @ApiModelProperty(value = "响应编码")
    protected final String code;

    @ApiModelProperty(value = "错误描述")
    protected final String errMsg;

    protected WebResult(final String code, final String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }
}