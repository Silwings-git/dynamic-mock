package top.silwings.admin.repository.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.admin.common.enums.MockHandlerComponentType;
import top.silwings.admin.repository.po.MockHandlerResponsePo;
import top.silwings.admin.repository.po.pack.MockHandlerResponsePoWrap;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockResponseInfoDto;
import top.silwings.core.utils.ConvertUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerResponseDaoConverter
 * @Description
 * @Author Silwings
 * @Date 2023/8/8 13:49
 * @Since
 **/
@Component
public class MockHandlerResponseDaoConverter {

    private final ConditionDaoConverter conditionDaoConverter;

    private final MockHandlerResponseItemDaoConverter mockHandlerResponseItemDaoConverter;

    public MockHandlerResponseDaoConverter(final ConditionDaoConverter conditionDaoConverter, final MockHandlerResponseItemDaoConverter mockHandlerResponseItemDaoConverter) {
        this.conditionDaoConverter = conditionDaoConverter;
        this.mockHandlerResponseItemDaoConverter = mockHandlerResponseItemDaoConverter;
    }

    public List<MockHandlerResponsePoWrap> listConvert(final Identity handlerId, final List<MockResponseInfoDto> responseInfoDtoList) {
        if (CollectionUtils.isEmpty(responseInfoDtoList)) {
            return Collections.emptyList();
        }
        return responseInfoDtoList.stream()
                .map(e -> this.convert2Wrap(handlerId, e))
                .collect(Collectors.toList());
    }

    private MockHandlerResponsePoWrap convert2Wrap(final Identity handlerId, final MockResponseInfoDto mockResponseInfoDto) {
        final MockHandlerResponsePoWrap wrap = new MockHandlerResponsePoWrap();
        wrap.setMockHandlerResponsePo(this.convert(handlerId, mockResponseInfoDto));
        wrap.setConditionPoList(this.conditionDaoConverter.listConvert(handlerId, MockHandlerComponentType.MOCK_HANDLER_RESPONSE, mockResponseInfoDto.getSupport()));
        wrap.setMockHandlerResponseItemPo(this.mockHandlerResponseItemDaoConverter.convert(handlerId, mockResponseInfoDto.getResponse()));
        return wrap;
    }

    private MockHandlerResponsePo convert(final Identity handlerId, final MockResponseInfoDto mockResponseInfoDto) {
        final MockHandlerResponsePo po = new MockHandlerResponsePo();
        po.setResponseId(ConvertUtils.getNoNullOrDefault(mockResponseInfoDto.getResponseId(), null, Identity::intValue));
        po.setHandlerId(ConvertUtils.getNoNullOrDefault(handlerId, null, Identity::intValue));
        po.setName(mockResponseInfoDto.getName());
        po.setDelayTime(mockResponseInfoDto.getDelayTime());

        return po;
    }
}