package top.silwings.admin.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.repository.MockHandlerRepository;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.handler.MockHandler;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.model.dto.MockHandlerDto;
import top.silwings.core.model.dto.QueryConditionDto;

import java.util.List;

/**
 * @ClassName MockHandlerApplication
 * @Description
 * @Author Silwings
 * @Date 2022/11/16 21:49
 * @Since
 **/
@Service
public class MockHandlerServiceImpl implements MockHandlerService {

    private final MockHandlerRepository mockHandlerRepository;

    private final MockHandlerManager mockHandlerManager;

    private final MockHandlerFactory mockHandlerFactory;

    public MockHandlerServiceImpl(final MockHandlerRepository mockHandlerRepository, final MockHandlerManager mockHandlerManager, final MockHandlerFactory mockHandlerFactory) {
        this.mockHandlerRepository = mockHandlerRepository;
        this.mockHandlerManager = mockHandlerManager;
        this.mockHandlerFactory = mockHandlerFactory;
    }

    public Identity save(final MockHandlerDto mockHandlerDto) {

        Identity handlerId;

        if (null == mockHandlerDto.getHandlerId()) {
            handlerId = this.mockHandlerRepository.create(mockHandlerDto);
        } else {
            handlerId = this.mockHandlerRepository.update(mockHandlerDto);
        }

        return handlerId;
    }

    public MockHandlerDto find(final Identity handlerId) {
        return this.mockHandlerRepository.find(handlerId);
    }

    public PageData<MockHandlerDto> query(final QueryConditionDto queryCondition, final PageParam pageParam) {
        return this.mockHandlerRepository.query(queryCondition, pageParam);
    }

    public void delete(final Identity handlerId) {
        this.mockHandlerRepository.delete(handlerId);
        this.mockHandlerManager.unregisterHandler(handlerId);
    }

    @Override
    public void delete(final List<Identity> handlerIdList) {
        if (CollectionUtils.isEmpty(handlerIdList)) {
            return;
        }

        this.mockHandlerRepository.delete(handlerIdList);
        handlerIdList.forEach(this.mockHandlerManager::unregisterHandler);
    }

    public void updateEnableStatus(final Identity handlerId, final EnableStatus enableStatus) {

        this.mockHandlerRepository.updateEnableStatus(handlerId, enableStatus);

        if (EnableStatus.ENABLE.equals(enableStatus)) {

            final MockHandlerDto mockHandlerDto = this.mockHandlerRepository.find(handlerId);

            final MockHandler mockHandler = this.mockHandlerFactory.buildMockHandler(mockHandlerDto);

            this.mockHandlerManager.registerHandler(mockHandler);

        } else {

            this.mockHandlerManager.unregisterHandler(handlerId);
        }
    }
}