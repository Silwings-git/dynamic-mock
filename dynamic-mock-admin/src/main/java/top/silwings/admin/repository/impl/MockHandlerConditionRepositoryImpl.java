package top.silwings.admin.repository.impl;

import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.enums.MockHandlerComponentType;
import top.silwings.admin.model.MockHandlerConditionRepository;
import top.silwings.admin.repository.mapper.MockHandlerConditionMapper;
import top.silwings.admin.repository.po.MockHandlerConditionPo;
import top.silwings.core.common.Identity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerConditionRepositoryImpl
 * @Description
 * @Author Silwings
 * @Date 2023/8/8 16:07
 * @Since
 **/
@Component
public class MockHandlerConditionRepositoryImpl implements MockHandlerConditionRepository {

    private final MockHandlerConditionMapper mockHandlerConditionMapper;

    public MockHandlerConditionRepositoryImpl(final MockHandlerConditionMapper mockHandlerConditionMapper) {
        this.mockHandlerConditionMapper = mockHandlerConditionMapper;
    }

    @Override
    public List<MockHandlerConditionPo> queryConditions(final Identity handlerId, final Identity componentId, final MockHandlerComponentType componentType) {
        final Example conditionExample = new Example(MockHandlerConditionPo.class, true, true);
        conditionExample.createCriteria()
                .andEqualTo(MockHandlerConditionPo.C_HANDLER_ID, handlerId.intValue())
                .andEqualTo(MockHandlerConditionPo.C_COMPONENT_ID, componentId.intValue())
                .andEqualTo(MockHandlerConditionPo.C_COMPONENT_TYPE, componentType);
        return this.mockHandlerConditionMapper.selectByCondition(conditionExample)
                .stream()
                .sorted(Comparator.comparingInt(MockHandlerConditionPo::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public int delete(final Identity handlerId, final MockHandlerComponentType mockHandlerComponentType) {
        final Example conditionExample = new Example(MockHandlerConditionPo.class, true, true);
        conditionExample.createCriteria()
                .andEqualTo(MockHandlerConditionPo.C_HANDLER_ID, handlerId.intValue())
                .andEqualTo(MockHandlerConditionPo.C_COMPONENT_TYPE, mockHandlerComponentType);
        return this.mockHandlerConditionMapper.deleteByCondition(conditionExample);
    }

    @Override
    public int delete(final Identity handlerId, final Identity componentId, final MockHandlerComponentType mockHandlerComponentType) {
        final Example conditionExample = new Example(MockHandlerConditionPo.class, true, true);
        conditionExample.createCriteria()
                .andEqualTo(MockHandlerConditionPo.C_HANDLER_ID, handlerId.intValue())
                .andEqualTo(MockHandlerConditionPo.C_COMPONENT_ID, componentId.intValue())
                .andEqualTo(MockHandlerConditionPo.C_COMPONENT_TYPE, mockHandlerComponentType);
        return this.mockHandlerConditionMapper.deleteByCondition(conditionExample);
    }

    @Override
    public int insertSelective(final Identity handlerId, final Identity componentId, final MockHandlerComponentType mockHandlerComponentType, final MockHandlerConditionPo mockHandlerConditionPo) {
        if (null == mockHandlerConditionPo) {
            return 0;
        }

        mockHandlerConditionPo.setHandlerId(handlerId.intValue());
        mockHandlerConditionPo.setComponentId(componentId.intValue());
        mockHandlerConditionPo.setComponentType(mockHandlerComponentType);
        return this.mockHandlerConditionMapper.insertSelective(mockHandlerConditionPo);
    }

}