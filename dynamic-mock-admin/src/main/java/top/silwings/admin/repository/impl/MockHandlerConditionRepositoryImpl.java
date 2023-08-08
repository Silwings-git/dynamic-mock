package top.silwings.admin.repository.impl;

import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.enums.MockHandlerComponentType;
import top.silwings.admin.model.MockHandlerConditionRepository;
import top.silwings.admin.repository.mapper.MockHandlerConditionMapper;
import top.silwings.admin.repository.po.ConditionPo;
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
    public List<ConditionPo> queryConditions(final Identity handlerId, final Identity componentId, final MockHandlerComponentType componentType) {
        final Example conditionExample = new Example(ConditionPo.class, true, true);
        conditionExample.createCriteria()
                .andEqualTo(ConditionPo.C_HANDLER_ID, handlerId.intValue())
                .andEqualTo(ConditionPo.C_COMPONENT_ID, componentId.intValue())
                .andEqualTo(ConditionPo.C_COMPONENT_TYPE, componentType);
        return this.mockHandlerConditionMapper.selectByCondition(conditionExample)
                .stream()
                .sorted(Comparator.comparingInt(ConditionPo::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public int deleteByHandlerId(final Identity handlerId) {
        final Example conditionExample = new Example(ConditionPo.class);
        conditionExample.createCriteria()
                .andEqualTo(ConditionPo.C_HANDLER_ID, handlerId.intValue());
        return this.mockHandlerConditionMapper.deleteByCondition(conditionExample);
    }

    @Override
    public int insertSelective(final ConditionPo conditionPo) {
        if (null == conditionPo) {
            return 0;
        }
        return this.mockHandlerConditionMapper.insertSelective(conditionPo);
    }

}