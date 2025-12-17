package cn.silwings.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
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
public class CheckInfoDto {

    /**
     * 错误信息
     */
    private List<ErrorResponseInfoDto> errResList;

    /**
     * 校验项
     */
    private List<CheckItemDto> checkItemList;

    public static CheckInfoDto newEmpty() {
        final CheckInfoDto checkInfoDto = new CheckInfoDto();
        checkInfoDto.setErrResList(Collections.emptyList());
        checkInfoDto.setCheckItemList(Collections.emptyList());
        return checkInfoDto;
    }

}