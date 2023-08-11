package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.core.utils.CheckUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ErrorResponseInfo
 * @Description
 * @Author Silwings
 * @Date 2023/8/11 11:52
 * @Since
 **/
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(description = "检验失败的响应")
public class ErrorResponseInfoParam {

    /**
     * 错误响应Code
     */
    @ApiModelProperty(value = "错误响应Code")
    private String errResCode;

    /**
     * http状态码
     */
    @ApiModelProperty(value = "http状态码")
    private Integer status;

    /**
     * 响应头信息
     */
    @ApiModelProperty(value = "响应头信息")
    private Map<String, List<String>> headers;

    /**
     * 响应体
     */
    @ApiModelProperty(value = "响应体")
    private Object body;

    public void validate() {
        CheckUtils.isNotBlank(this.errResCode, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "errResCode"));
        CheckUtils.isNotNull(this.status, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "status"));
    }
}