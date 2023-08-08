package top.silwings.admin.model;

import top.silwings.admin.common.enums.MockHandlerComponentType;
import top.silwings.admin.repository.po.ConditionPo;
import top.silwings.core.common.Identity;

import java.util.List;

/**
 * @ClassName MockHandlerConditionRepository
 * @Description
 * @Author Silwings
 * @Date 2023/8/8 16:07
 * @Since
 **/
public interface MockHandlerConditionRepository {
    List<ConditionPo> queryConditions(Identity handlerId, Identity componentId, MockHandlerComponentType componentType);

    int deleteByHandlerId(Identity handlerId);

    int insertSelective(ConditionPo conditionPo);
}