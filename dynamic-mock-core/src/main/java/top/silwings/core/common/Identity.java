package top.silwings.core.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName Identity
 * @Description ID
 * @Author Silwings
 * @Date 2022/11/14 22:08
 * @Since
 **/
@Getter
@Setter
public class Identity {

    /**
     * 唯一标识符
     */
    @ApiModelProperty(value = "唯一标识符",example = "Code001")
    private String id;

}