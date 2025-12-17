package cn.silwings.admin.repository.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import cn.silwings.admin.common.enums.MockHandlerComponentType;
import cn.silwings.admin.repository.po.MockHandlerResponsePo;
import cn.silwings.admin.repository.po.pack.MockHandlerResponsePoWrap;
import cn.silwings.admin.utils.Counter;
import cn.silwings.core.common.EnableStatus;
import cn.silwings.core.common.Identity;
import cn.silwings.core.model.MockResponseInfoDto;
import cn.silwings.core.utils.ConvertUtils;
import cn.silwings.core.utils.JsonUtils;

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
        final Counter sort = Counter.newInstance();
        return responseInfoDtoList.stream()
                .map(e -> this.convert2Wrap(handlerId, e, sort.increment()))
                .collect(Collectors.toList());
    }

    private MockHandlerResponsePoWrap convert2Wrap(final Identity handlerId, final MockResponseInfoDto mockResponseInfoDto, final Integer sort) {
        final MockHandlerResponsePoWrap wrap = new MockHandlerResponsePoWrap();
        wrap.setMockHandlerResponsePo(this.convert(handlerId, mockResponseInfoDto, sort));
        wrap.setMockHandlerConditionPoList(this.conditionDaoConverter.listConvert(handlerId, MockHandlerComponentType.MOCK_HANDLER_RESPONSE, mockResponseInfoDto.getSupport()));
        wrap.setMockHandlerResponseItemPo(this.mockHandlerResponseItemDaoConverter.convert(handlerId, mockResponseInfoDto.getResponse()));
        return wrap;
    }

    public MockHandlerResponsePo convert(final Identity handlerId, final MockResponseInfoDto mockResponseInfoDto, final Integer sort) {
        final MockHandlerResponsePo po = new MockHandlerResponsePo();
        po.setResponseId(ConvertUtils.getNoNullOrDefault(mockResponseInfoDto.getResponseId(), null, Identity::intValue));
        po.setHandlerId(ConvertUtils.getNoNullOrDefault(handlerId, null, Identity::intValue));
        po.setName(mockResponseInfoDto.getName());
        po.setEnableStatus(ConvertUtils.getNoNullOrDefault(mockResponseInfoDto.getEnableStatus(), null, EnableStatus::code));
        po.setDelayTime(mockResponseInfoDto.getDelayTime());
        po.setCheckInfoJson(JsonUtils.toJSONString(ConvertUtils.getNoNullOrDefault(mockResponseInfoDto.getCheckInfo(), JsonUtils.EMPTY_OBJECT)));
        po.setSort(sort);

        return po;
    }
}