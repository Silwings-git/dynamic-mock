package top.silwings.admin.repository.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.admin.common.enums.MockHandlerComponentType;
import top.silwings.admin.repository.po.ConditionPo;
import top.silwings.core.common.Identity;
import top.silwings.core.utils.ConvertUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ConditionDaoConverter
 * @Description
 * @Author Silwings
 * @Date 2023/8/7 17:39
 * @Since
 **/
@Component
public class ConditionDaoConverter {
    public List<ConditionPo> listConvert(final Identity handlerId, final MockHandlerComponentType componentType, final List<String> expressionList) {
        if (CollectionUtils.isEmpty(expressionList)) {
            return Collections.emptyList();
        }

        return expressionList.stream().map(e -> this.convert(handlerId, componentType, e)).collect(Collectors.toList());
    }

    private ConditionPo convert(final Identity handlerId, final MockHandlerComponentType componentType, final String expression) {
        final ConditionPo po = new ConditionPo();
        po.setConditionId(null);
        po.setHandlerId(ConvertUtils.getNoNullOrDefault(handlerId, null, Identity::intValue));
        po.setComponentId(null);
        po.setComponentType(componentType);
        po.setExpression(expression);
        return po;
    }
}