package top.silwings.admin.web.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.silwings.admin.web.vo.param.MockHandlerInfoParam;

import java.util.Date;

/**
 * @ClassName MockHandlerInfoResultVo
 * @Description Mock处理器信息
 * @Author Silwings
 * @Date 2022/11/16 21:14
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "模拟处理器信息")
public class MockHandlerInfoResult extends MockHandlerInfoParam {

    @ApiModelProperty(value = "启用状态", example = "enable")
    private Integer enableStatus;

    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;

}