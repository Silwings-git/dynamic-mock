package top.silwings.admin.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.events.DeleteMockHandlerEvent;
import top.silwings.admin.events.DeleteProjectEvent;
import top.silwings.admin.events.SaveMockHandlerEvent;
import top.silwings.admin.model.Project;
import top.silwings.admin.model.ProjectMockHandler;
import top.silwings.admin.repository.MockHandlerRepository;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.handler.MockHandler;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.model.dto.MockHandlerDto;
import top.silwings.core.model.dto.QueryConditionDto;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerApplication
 * @Description
 * @Author Silwings
 * @Date 2022/11/16 21:49
 * @Since
 **/
@Service
public class MockHandlerServiceImpl implements MockHandlerService, ApplicationListener<DeleteProjectEvent> {

    private final MockHandlerRepository mockHandlerRepository;

    private final MockHandlerManager mockHandlerManager;

    private final MockHandlerFactory mockHandlerFactory;

    private final ApplicationEventPublisher applicationEventPublisher;

    public MockHandlerServiceImpl(final MockHandlerRepository mockHandlerRepository, final MockHandlerManager mockHandlerManager, final MockHandlerFactory mockHandlerFactory, final ApplicationEventPublisher applicationEventPublisher) {
        this.mockHandlerRepository = mockHandlerRepository;
        this.mockHandlerManager = mockHandlerManager;
        this.mockHandlerFactory = mockHandlerFactory;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public Identity save(final MockHandlerDto mockHandlerDto, final Identity projectId) {

        Identity handlerId;

        if (null == mockHandlerDto.getHandlerId()) {
            handlerId = this.mockHandlerRepository.create(mockHandlerDto);
        } else {
            handlerId = this.mockHandlerRepository.update(mockHandlerDto);
        }

        if (null != handlerId) {
            this.applicationEventPublisher.publishEvent(SaveMockHandlerEvent.of(this, handlerId, projectId));
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
        final boolean deleted = this.mockHandlerRepository.delete(handlerId);
        this.mockHandlerManager.unregisterHandler(handlerId);
        if (deleted) {
            this.applicationEventPublisher.publishEvent(DeleteMockHandlerEvent.of(this, handlerId));
        }
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

    @Override
    public void onApplicationEvent(final DeleteProjectEvent event) {

        final Project project = event.getProject();

        final Set<Identity> handlerIdSet = project.getProjectMockHandlerList().stream()
                .map(ProjectMockHandler::getHandlerId)
                .collect(Collectors.toSet());

        this.mockHandlerRepository.delete(handlerIdSet);
    }
}