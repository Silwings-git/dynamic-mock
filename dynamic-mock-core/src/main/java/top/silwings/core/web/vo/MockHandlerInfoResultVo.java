package top.silwings.core.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class MockHandlerInfoResultVo extends MockHandlerInfoVo {

    @ApiModelProperty(value = "启用状态", required = true, example = "enable")
    private String enableStatus;

}