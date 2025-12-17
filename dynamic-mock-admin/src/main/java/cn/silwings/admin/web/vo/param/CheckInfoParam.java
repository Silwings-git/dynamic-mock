package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.core.utils.CheckUtils;

import java.util.List;

/**
 * @ClassName CheckInfoDto
 * @Description
 * @Author Silwings
 * @Date 2023/8/11 13:40
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "检查信息")
public class CheckInfoParam {

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误响应列表")
    private List<ErrorResponseInfoParam> errResList;

    /**
     * 校验项
     */
    @ApiModelProperty(value = "校验项")
    private List<CheckItemParam> checkItemList;

    public void validate() {
        if (CollectionUtils.isNotEmpty(this.errResList)) {
            this.errResList.forEach(ErrorResponseInfoParam::validate);
            final long count = this.errResList.stream().map(ErrorResponseInfoParam::getErrResCode).distinct().count();
            CheckUtils.isTrue(count == this.errResList.size(), DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "errResCode"));
        }
        if (CollectionUtils.isNotEmpty(this.checkItemList)) {
            this.checkItemList.forEach(CheckItemParam::validate);
        }
    }

}