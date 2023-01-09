package top.silwings.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;

import java.util.List;

/**
 * @ClassName QueryConditionDto
 * @Description 查询条件
 * @Author Silwings
 * @Date 2022/11/16 21:28
 * @Since
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryHandlerConditionDto {

    /**
     * 项目id集.如果为null表示允许查询所有
     */
    private List<Identity> projectIdList;

    /**
     * 项目id
     */
    private Identity projectId;

    /**
     * 处理器名称右模糊
     */
    private String name;

    /**
     * 处理器状态
     */
    private EnableStatus enableStatus;

    /**
     * 请求方法
     */
    private String httpMethod;

    /**
     * 支持的请求URI右模糊
     */
    private String requestUri;

    /**
     * 模拟处理器标签
     */
    private String label;

}