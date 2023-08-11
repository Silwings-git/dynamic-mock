package top.silwings.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckInfoDto
 * @Description
 * @Author Silwings
 * @Date 2023/8/11 13:40
 * @Since
 **/
@Getter
@Setter
public class CheckInfoDto {

    /**
     * 错误信息
     */
    private Map<String, ErrorResponseInfoDto> errResMap;

    /**
     * 校验项
     */
    private List<CheckItemDto> checkItemList;

}