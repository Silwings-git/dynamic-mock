package top.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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

}