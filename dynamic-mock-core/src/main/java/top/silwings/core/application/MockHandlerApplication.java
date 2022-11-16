package top.silwings.core.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.common.PageData;
import top.silwings.core.common.PageParam;
import top.silwings.core.handler.MockHandler;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.repository.MockHandlerRepository;
import top.silwings.core.repository.dto.MockHandlerDto;
import top.silwings.core.repository.dto.QueryConditionDto;

/**
 * @ClassName MockHandlerApplication
 * @Description
 * @Author Silwings
 * @Date 2022/11/16 21:49
 * @Since
 **/
@Service
public class MockHandlerApplication {

    private final MockHandlerRepository mockHandlerRepository;

    private final MockHandlerManager mockHandlerManager;

    private final MockHandlerFactory mockHandlerFactory;

    public MockHandlerApplication(final MockHandlerRepository mockHandlerRepository, final MockHandlerManager mockHandlerManager, final MockHandlerFactory mockHandlerFactory) {
        this.mockHandlerRepository = mockHandlerRepository;
        this.mockHandlerManager = mockHandlerManager;
        this.mockHandlerFactory = mockHandlerFactory;
    }

    @Transactional
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


    @Transactional
    public void delete(final Identity handlerId) {
        this.mockHandlerRepository.delete(handlerId);
        this.mockHandlerManager.unregisterHandler(handlerId);
    }

    @Transactional
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