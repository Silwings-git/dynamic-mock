package top.silwings.admin.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * @ClassName Result
 * @Description Web Result
 * @Author Silwings
 * @Date 2022/11/14 23:50
 * @Since
 **/
@Getter
@ApiModel(description = "Result")
public class Result<T> extends WebResult {

    @ApiModelProperty(value = "数据")
    private final T data;

    public Result(final String code, final String errMsg, final T data) {
        super(code, errMsg);
        this.data = data;
    }

    public static <T> Result<T> ok() {
        return new Result<>(SUCCESS, null, null);
    }

    public static <T> Result<T> ok(final T data) {
        return new Result<>(SUCCESS, null, data);
    }

    public static <T> Result<T> fail(final String errMsg) {
        return new Result<>(FAIL, errMsg, null);
    }

}