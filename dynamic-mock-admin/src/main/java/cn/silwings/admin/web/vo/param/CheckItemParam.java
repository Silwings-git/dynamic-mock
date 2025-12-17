package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.core.utils.CheckUtils;

import java.util.List;

/**
 * @ClassName CheckItem
 * @Description
 * @Author Silwings
 * @Date 2023/8/11 10:20
 * @Since
 **/
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(description = "校验项目")
public class CheckItemParam {

    /**
     * 错误消息Id
     */
    @ApiModelProperty(value = "错误消息Id", required = true, example = "1")
    private String errResCode;

    /**
     * 错误消息填充参数
     */
    @ApiModelProperty(value = "错误消息填充参数")
    private List<String> errMsgFillParam;

    /**
     * 检查语句
     */
    @ApiModelProperty(value = "检查语句,可以使用动态表达式", required = true, example = "true")
    private String checkExpression;

    public void validate() {
        CheckUtils.isNotBlank(this.checkExpression, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "checkExpression"));
    }
}