package top.silwings.admin.repository.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;
import top.silwings.admin.repository.po.ConditionPo;
import top.silwings.admin.repository.po.MockHandlerResponseItemPo;
import top.silwings.admin.repository.po.MockHandlerResponsePo;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockResponseDto;
import top.silwings.core.model.MockResponseInfoDto;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerResponseItemDaoConverter
 * @Description
 * @Author Silwings
 * @Date 2023/8/8 14:02
 * @Since
 **/
@Component
public class MockHandlerResponseItemDaoConverter {

    public MockHandlerResponseItemPo convert(final Identity handlerId, final MockResponseDto mockResponseDto) {

        final MockHandlerResponseItemPo po = new MockHandlerResponseItemPo();
        po.setResponseItemId(null);
        po.setResponseId(null);
        po.setHandlerId(ConvertUtils.getNoNullOrDefault(handlerId, null, Identity::intValue));
        po.setStatus(mockResponseDto.getStatus());
        po.setHeaders(ConvertUtils.getNoBlankOrDefault(JsonUtils.toJSONString(mockResponseDto.getHeaders()), "{}"));
        po.setBody(JsonUtils.toJSONString(mockResponseDto.getBody()));

        return po;
    }

    public MockResponseInfoDto convert(final MockHandlerResponsePo mockHandlerResponsePo, final List<ConditionPo> conditionPoList, final MockHandlerResponseItemPo responseItemPo) {
        return MockResponseInfoDto.builder()
                .responseId(Identity.from(mockHandlerResponsePo.getResponseId()))
                .name(mockHandlerResponsePo.getName())
                .support(conditionPoList.stream().map(ConditionPo::getExpression).collect(Collectors.toList()))
                .delayTime(mockHandlerResponsePo.getDelayTime())
                .response(this.convert(responseItemPo))
                .build();
    }

    private MockResponseDto convert(final MockHandlerResponseItemPo responseItemPo) {
        return MockResponseDto.builder()
                .status(responseItemPo.getStatus())
                .headers(JsonUtils.nativeRead(ConvertUtils.getNoBlankOrDefault(responseItemPo.getHeaders(), "{}"), new TypeReference<Map<String, List<String>>>() {
                }))
                .body(JsonUtils.tryToBean(responseItemPo.getBody()))
                .build();
    }
}