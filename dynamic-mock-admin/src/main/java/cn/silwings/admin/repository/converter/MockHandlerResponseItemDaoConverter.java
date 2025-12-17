package cn.silwings.admin.repository.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;
import cn.silwings.admin.repository.po.MockHandlerConditionPo;
import cn.silwings.admin.repository.po.MockHandlerResponseItemPo;
import cn.silwings.admin.repository.po.MockHandlerResponsePo;
import cn.silwings.core.common.EnableStatus;
import cn.silwings.core.common.Identity;
import cn.silwings.core.model.CheckInfoDto;
import cn.silwings.core.model.MockResponseDto;
import cn.silwings.core.model.MockResponseInfoDto;
import cn.silwings.core.utils.ConvertUtils;
import cn.silwings.core.utils.JsonUtils;

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
        po.setHeaders(ConvertUtils.getNoBlankOrDefault(JsonUtils.toJSONString(mockResponseDto.getHeaders()), JsonUtils.EMPTY_OBJECT));
        po.setBody(JsonUtils.toJSONString(mockResponseDto.getBody()));

        return po;
    }

    public MockResponseInfoDto convert(final MockHandlerResponsePo mockHandlerResponsePo, final List<MockHandlerConditionPo> mockHandlerConditionPoList, final MockHandlerResponseItemPo responseItemPo) {
        return MockResponseInfoDto.builder()
                .responseId(Identity.from(mockHandlerResponsePo.getResponseId()))
                .name(mockHandlerResponsePo.getName())
                .enableStatus(EnableStatus.valueOfCode(mockHandlerResponsePo.getEnableStatus()))
                .support(mockHandlerConditionPoList.stream().map(MockHandlerConditionPo::getExpression).collect(Collectors.toList()))
                .delayTime(mockHandlerResponsePo.getDelayTime())
                .checkInfo(JsonUtils.tryToBean(mockHandlerResponsePo.getCheckInfoJson(), CheckInfoDto.class, CheckInfoDto::newEmpty))
                .response(this.convert(responseItemPo))
                .build();
    }

    private MockResponseDto convert(final MockHandlerResponseItemPo responseItemPo) {
        return MockResponseDto.builder()
                .status(responseItemPo.getStatus())
                .headers(JsonUtils.nativeRead(ConvertUtils.getNoBlankOrDefault(responseItemPo.getHeaders(), JsonUtils.EMPTY_OBJECT), new TypeReference<Map<String, List<String>>>() {
                }))
                .body(JsonUtils.tryToBean(responseItemPo.getBody()))
                .build();
    }
}