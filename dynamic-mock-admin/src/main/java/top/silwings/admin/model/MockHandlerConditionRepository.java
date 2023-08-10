package top.silwings.admin.model;

import top.silwings.admin.common.enums.MockHandlerComponentType;
import top.silwings.admin.repository.po.MockHandlerConditionPo;
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
    List<MockHandlerConditionPo> queryConditions(Identity handlerId, Identity componentId, MockHandlerComponentType componentType);

    int delete(Identity handlerId, MockHandlerComponentType mockHandlerComponentType);

    int delete(Identity handlerId, Identity componentId, MockHandlerComponentType mockHandlerComponentType);

    int insertSelective(Identity handlerId, Identity componentId, MockHandlerComponentType mockHandlerComponentType, MockHandlerConditionPo conditionPo);

}