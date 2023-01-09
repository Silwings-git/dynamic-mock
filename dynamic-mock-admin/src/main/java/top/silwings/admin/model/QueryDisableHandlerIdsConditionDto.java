package top.silwings.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.silwings.core.common.Identity;

import java.util.Collection;

/**
 * @ClassName QueryDisableHandlerConditionDto
 * @Description 查询禁用处理器id集条件参数
 * @Author Silwings
 * @Date 2023/1/9 21:35
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryDisableHandlerIdsConditionDto {

    /**
     * 指定查询的处理器的id范围
     */
    private Collection<Identity> handlerIdRangeList;

}