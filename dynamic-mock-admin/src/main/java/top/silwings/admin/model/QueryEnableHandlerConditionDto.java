package top.silwings.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.silwings.core.common.Identity;

import java.util.Collection;

/**
 * @ClassName QueryEnableHandlerConditionDto
 * @Description 启用处理器查询参数
 * @Author Silwings
 * @Date 2023/1/9 20:47
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryEnableHandlerConditionDto {

    /**
     * 要排除的处理器id集
     */
    private Collection<Identity> excludeHandlerIdList;

    /**
     * 指定查询的处理器的id范围
     */
    private Collection<Identity> handlerIdRangeList;

    /**
     * 项目id
     */
    private Identity projectId;

}