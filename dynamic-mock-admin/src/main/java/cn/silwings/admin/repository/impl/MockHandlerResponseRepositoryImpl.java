package cn.silwings.admin.repository.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import cn.silwings.admin.common.enums.MockHandlerComponentType;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.admin.model.MockHandlerConditionRepository;
import cn.silwings.admin.repository.MockHandlerResponseRepository;
import cn.silwings.admin.repository.converter.ConditionDaoConverter;
import cn.silwings.admin.repository.converter.MockHandlerResponseDaoConverter;
import cn.silwings.admin.repository.converter.MockHandlerResponseItemDaoConverter;
import cn.silwings.admin.repository.mapper.MockHandlerResponseItemMapper;
import cn.silwings.admin.repository.mapper.MockHandlerResponseMapper;
import cn.silwings.admin.repository.po.MockHandlerConditionPo;
import cn.silwings.admin.repository.po.MockHandlerResponseItemPo;
import cn.silwings.admin.repository.po.MockHandlerResponsePo;
import cn.silwings.admin.repository.po.pack.MockHandlerResponsePoWrap;
import cn.silwings.core.common.EnableStatus;
import cn.silwings.core.common.Identity;
import cn.silwings.core.model.MockResponseInfoDto;
import cn.silwings.core.utils.CheckUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerResponseRepositoryImpl
 * @Description
 * @Author Silwings
 * @Date 2023/8/8 13:43
 * @Since
 **/
@Component
public class MockHandlerResponseRepositoryImpl implements MockHandlerResponseRepository {

    private final MockHandlerResponseMapper mockHandlerResponseMapper;

    private final MockHandlerResponseItemMapper mockHandlerResponseItemMapper;

    private final MockHandlerConditionRepository mockHandlerConditionRepository;

    private final MockHandlerResponseDaoConverter mockHandlerResponseDaoConverter;

    private final MockHandlerResponseItemDaoConverter mockHandlerResponseItemDaoConverter;

    private final ConditionDaoConverter conditionDaoConverter;

    public MockHandlerResponseRepositoryImpl(final MockHandlerResponseMapper mockHandlerResponseMapper, final MockHandlerResponseItemMapper mockHandlerResponseItemMapper, final MockHandlerConditionRepository mockHandlerConditionRepository, final MockHandlerResponseDaoConverter mockHandlerResponseDaoConverter, final MockHandlerResponseItemDaoConverter mockHandlerResponseItemDaoConverter, final ConditionDaoConverter conditionDaoConverter) {
        this.mockHandlerResponseMapper = mockHandlerResponseMapper;
        this.mockHandlerResponseItemMapper = mockHandlerResponseItemMapper;
        this.mockHandlerConditionRepository = mockHandlerConditionRepository;
        this.mockHandlerResponseDaoConverter = mockHandlerResponseDaoConverter;
        this.mockHandlerResponseItemDaoConverter = mockHandlerResponseItemDaoConverter;
        this.conditionDaoConverter = conditionDaoConverter;
    }

    @Override
    public List<MockResponseInfoDto> queryMockHandlerResponseList(final Identity handlerId) {

        final Example responseExample = new Example(MockHandlerResponsePo.class);
        responseExample.createCriteria()
                .andEqualTo(MockHandlerResponsePo.C_HANDLER_ID, handlerId.intValue());

        return this.mockHandlerResponseMapper.selectByCondition(responseExample)
                .stream()
                .sorted(Comparator.comparingInt(MockHandlerResponsePo::getSort))
                .map(mockHandlerResponsePo -> {
                    final Identity responseId = Identity.from(mockHandlerResponsePo.getResponseId());

                    // 条件信息
                    final List<MockHandlerConditionPo> mockHandlerConditionPoList = this.mockHandlerConditionRepository.queryConditions(handlerId, responseId, MockHandlerComponentType.MOCK_HANDLER_RESPONSE);

                    // 请求信息
                    final MockHandlerResponseItemPo responseItemPo = this.findResponseItem(handlerId, responseId);

                    return this.mockHandlerResponseItemDaoConverter.convert(mockHandlerResponsePo, mockHandlerConditionPoList, responseItemPo);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteMockHandlerResponse(final Identity handlerId) {

        final Example responseExample = new Example(MockHandlerResponsePo.class);
        responseExample.createCriteria()
                .andEqualTo(MockHandlerResponsePo.C_HANDLER_ID, handlerId.intValue());
        this.mockHandlerResponseMapper.deleteByCondition(responseExample);

        this.mockHandlerConditionRepository.delete(handlerId, MockHandlerComponentType.MOCK_HANDLER_RESPONSE);

        final Example itemExample = new Example(MockHandlerResponseItemPo.class);
        itemExample.createCriteria()
                .andEqualTo(MockHandlerResponseItemPo.C_HANDLER_ID, handlerId.intValue());
        this.mockHandlerResponseItemMapper.deleteByCondition(itemExample);

        return true;
    }

    @Override
    @Transactional
    public void insertMockHandlerResponse(final List<MockHandlerResponsePoWrap> mockHandlerResponsePoWrapList) {

        if (CollectionUtils.isEmpty(mockHandlerResponsePoWrapList)) {
            return;
        }

        for (final MockHandlerResponsePoWrap wrap : mockHandlerResponsePoWrapList) {
            final MockHandlerResponsePo responsePo = wrap.getMockHandlerResponsePo();
            this.mockHandlerResponseMapper.insertSelective(responsePo);

            wrap.getMockHandlerConditionPoList().forEach(e ->
                    this.mockHandlerConditionRepository.insertSelective(Identity.from(responsePo.getHandlerId()),
                            Identity.from(responsePo.getResponseId()),
                            MockHandlerComponentType.MOCK_HANDLER_RESPONSE, e)
            );

            final MockHandlerResponseItemPo mockHandlerResponseItemPo = wrap.getMockHandlerResponseItemPo();
            mockHandlerResponseItemPo.setHandlerId(responsePo.getHandlerId());
            mockHandlerResponseItemPo.setResponseId(responsePo.getResponseId());
            this.mockHandlerResponseItemMapper.insertSelective(mockHandlerResponseItemPo);
        }
    }

    private MockHandlerResponseItemPo findResponseItem(final Identity handlerId, final Identity responseId) {
        return this.mockHandlerResponseItemMapper.selectOne(new MockHandlerResponseItemPo()
                .setHandlerId(handlerId.intValue())
                .setResponseId(responseId.intValue()));
    }

    @Override
    @Transactional
    public void updateByHandlerAndResponseId(final Identity handlerId, final MockResponseInfoDto responseInfoDto) {

        if (null == handlerId || null == responseInfoDto || null == responseInfoDto.getResponseId()) {
            return;
        }

        final Identity responseId = responseInfoDto.getResponseId();

        final MockHandlerResponsePo oldResponse = this.mockHandlerResponseMapper.findPoForUpdate(handlerId.intValue(), responseId.intValue());
        if (null == oldResponse) {
            throw DynamicMockAdminException.from(ErrorCode.MOCK_HANDLER_RESPONSE_NOT_EXIST);
        }

        final MockHandlerResponsePo responsePo = this.mockHandlerResponseDaoConverter.convert(handlerId, responseInfoDto, null);
        final Example responseExample = new Example(MockHandlerResponsePo.class);
        responseExample.createCriteria()
                .andEqualTo(MockHandlerResponsePo.C_HANDLER_ID, handlerId.intValue())
                .andEqualTo(MockHandlerResponsePo.C_RESPONSE_ID, responseId.intValue());
        this.mockHandlerResponseMapper.updateByConditionSelective(responsePo, responseExample);

        // 条件需要先删后增
        this.mockHandlerConditionRepository.delete(handlerId, responseId, MockHandlerComponentType.MOCK_HANDLER_RESPONSE);
        this.conditionDaoConverter.listConvert(handlerId, MockHandlerComponentType.MOCK_HANDLER_RESPONSE, responseInfoDto.getSupport())
                .forEach(e ->
                        this.mockHandlerConditionRepository.insertSelective(Identity.from(responsePo.getHandlerId()),
                                Identity.from(responsePo.getResponseId()),
                                MockHandlerComponentType.MOCK_HANDLER_RESPONSE, e)
                );

        final MockHandlerResponseItemPo responseItemPo = this.mockHandlerResponseItemDaoConverter.convert(handlerId, responseInfoDto.getResponse());
        final Example itemExample = new Example(MockHandlerResponseItemPo.class);
        itemExample.createCriteria()
                .andEqualTo(MockHandlerResponseItemPo.C_HANDLER_ID, handlerId.intValue())
                .andEqualTo(MockHandlerResponseItemPo.C_RESPONSE_ID, responseId.intValue());
        this.mockHandlerResponseItemMapper.updateByConditionSelective(responseItemPo, itemExample);
    }

    @Override
    public void updateResponseEnableStatus(final Identity handlerId, final Identity responseId, final EnableStatus enableStatus) {

        final Example example = new Example(MockHandlerResponsePo.class);
        example.createCriteria()
                .andEqualTo(MockHandlerResponsePo.C_HANDLER_ID, handlerId.intValue())
                .andEqualTo(MockHandlerResponsePo.C_RESPONSE_ID, responseId.intValue());

        final MockHandlerResponsePo newResponseStatus = new MockHandlerResponsePo();
        newResponseStatus.setEnableStatus(enableStatus.code());

        final int row = this.mockHandlerResponseMapper.updateByConditionSelective(newResponseStatus, example);
        CheckUtils.isTrue(row > 0, DynamicMockAdminException.supplier(ErrorCode.MOCK_HANDLER_RESPONSE_NOT_EXIST));
    }
}