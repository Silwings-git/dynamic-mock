package top.silwings.admin.repository.po;

import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.common.enums.MockHandlerComponentType;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName MockHandlerConditionPo
 * @Description MockHandler condition
 * @Author Silwings
 * @Date 2023/8/7 14:07
 * @Since
 **/
@Getter
@Setter
@Table(name = "dm_mock_handler_condition")
public class ConditionPo {

    public static final String C_COMPONENT_ID = "componentId";
    public static final String C_COMPONENT_TYPE = "componentType";
    public static final String C_HANDLER_ID = "handlerId";

    /**
     * 唯一标识
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer conditionId;

    /**
     * mock handler id
     */
    @Column(name = "handler_id")
    private Integer handlerId;

    /**
     * 组件id
     */
    @Column(name = "component_id")
    private Integer componentId;

    /**
     * 组件类型
     */
    @Column(name = "component_type")
    private MockHandlerComponentType componentType;

    /**
     * 表达式
     */
    @Column(name = "expression")
    private String expression;

    /**
     * 排序
     */
    @Column(name = "sort")
    private int sort;

}