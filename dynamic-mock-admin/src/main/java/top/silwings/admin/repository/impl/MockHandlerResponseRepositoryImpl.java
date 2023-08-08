package top.silwings.admin.repository.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.silwings.admin.common.enums.MockHandlerComponentType;
import top.silwings.admin.repository.MockHandlerResponseRepository;
import top.silwings.admin.repository.converter.MockHandlerResponseItemDaoConverter;
import top.silwings.admin.repository.mapper.ConditionMapper;
import top.silwings.admin.repository.mapper.MockHandlerResponseItemMapper;
import top.silwings.admin.repository.mapper.MockHandlerResponseMapper;
import top.silwings.admin.repository.po.ConditionPo;
import top.silwings.admin.repository.po.MockHandlerResponseItemPo;
import top.silwings.admin.repository.po.MockHandlerResponsePo;
import top.silwings.admin.repository.po.pack.MockHandlerResponsePoWrap;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockResponseInfoDto;

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

    private final ConditionMapper conditionMapper;

    private final MockHandlerResponseItemDaoConverter mockHandlerResponseItemDaoConverter;

    public MockHandlerResponseRepositoryImpl(final MockHandlerResponseMapper mockHandlerResponseMapper, final MockHandlerResponseItemMapper mockHandlerResponseItemMapper, final ConditionMapper conditionMapper, final MockHandlerResponseItemDaoConverter mockHandlerResponseItemDaoConverter) {
        this.mockHandlerResponseMapper = mockHandlerResponseMapper;
        this.mockHandlerResponseItemMapper = mockHandlerResponseItemMapper;
        this.conditionMapper = conditionMapper;
        this.mockHandlerResponseItemDaoConverter = mockHandlerResponseItemDaoConverter;
    }

    @Override
    public List<MockResponseInfoDto> queryMockHandlerResponseList(final Identity handlerId) {

        final Example responseExample = new Example(MockHandlerResponsePo.class);
        responseExample.createCriteria()
                .andEqualTo(MockHandlerResponsePo.C_HANDLER_ID, handlerId.intValue());

        return this.mockHandlerResponseMapper.selectByCondition(responseExample)
                .stream()
                .map(mockHandlerResponsePo -> {
                    final Identity responseId = Identity.from(mockHandlerResponsePo.getResponseId());

                    // 条件信息
                    final List<ConditionPo> conditionPoList = this.queryConditions(handlerId, responseId);

                    // 请求信息
                    final MockHandlerResponseItemPo responseItemPo = this.findResponseItem(handlerId, responseId);

                    return this.mockHandlerResponseItemDaoConverter.convert(mockHandlerResponsePo, conditionPoList, responseItemPo);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean removeMockHandlerResponse(final Identity handlerId) {

        final Example responseExample = new Example(MockHandlerResponsePo.class);
        responseExample.createCriteria()
                .andEqualTo(MockHandlerResponsePo.C_HANDLER_ID, handlerId.intValue());
        this.mockHandlerResponseMapper.deleteByCondition(responseExample);

        final Example conditionExample = new Example(ConditionPo.class);
        conditionExample.createCriteria()
                .andEqualTo(ConditionPo.C_HANDLER_ID, handlerId.intValue());
        this.conditionMapper.deleteByCondition(conditionExample);

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

            wrap.getConditionPoList().forEach(e -> {
                e.setHandlerId(responsePo.getHandlerId());
                e.setComponentId(responsePo.getResponseId());
                e.setComponentType(MockHandlerComponentType.MOCK_HANDLER_RESPONSE);
                this.conditionMapper.insertSelective(e);
            });

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

    private List<ConditionPo> queryConditions(final Identity handlerId, final Identity responseId) {
        final Example conditionExample = new Example(ConditionPo.class);
        conditionExample.createCriteria()
                .andEqualTo(ConditionPo.C_HANDLER_ID, handlerId.intValue())
                .andEqualTo(ConditionPo.C_COMPONENT_ID, responseId.intValue())
                .andEqualTo(ConditionPo.C_COMPONENT_TYPE, MockHandlerComponentType.MOCK_HANDLER_RESPONSE);
        return this.conditionMapper.selectByCondition(conditionExample);
    }

}