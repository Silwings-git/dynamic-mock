package top.silwings.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
public class CheckItemDto {

    /**
     * 错误消息Id
     */
    private String errResCode;

    /**
     * 错误消息填充参数
     */
    private List<String> errMsgFillParam;

    /**
     * 检查语句
     */
    private String checkExpression;

}