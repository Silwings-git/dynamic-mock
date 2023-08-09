package top.silwings.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.silwings.admin.common.DynamicMockAdminContext;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.ProjectDto;
import top.silwings.admin.model.QueryDisableHandlerIdsConditionDto;
import top.silwings.admin.model.QueryEnableHandlerConditionDto;
import top.silwings.admin.model.TextFile;
import top.silwings.admin.service.FileService;
import top.silwings.admin.service.MockHandlerRegisterService;
import top.silwings.core.common.Identity;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.JsonUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerRegisterServiceImpl
 * @Description 处理器注册服务
 * @Author Silwings
 * @Date 2023/1/9 20:56
 * @Since
 **/
@Slf4j
@Service
public class MockHandlerRegisterServiceImpl implements MockHandlerRegisterService, ApplicationRunner {

    private final MockHandlerManager mockHandlerManager;

    private final MockHandlerFactory mockHandlerFactory;

    private final FileService fileService;

    public MockHandlerRegisterServiceImpl(final MockHandlerManager mockHandlerManager, final MockHandlerFactory mockHandlerFactory, final FileService fileService) {
        this.mockHandlerManager = mockHandlerManager;
        this.mockHandlerFactory = mockHandlerFactory;
        this.fileService = fileService;
    }

    /**
     * 初始化全部启用的MockHandler并注册
     */
    @Override
    public void run(final ApplicationArguments args) throws Exception {
        this.registerAllEnableHandler(true);
        log.info("Dynamic Mock Handler initialized.");
    }

    @Override
    public synchronized void registerAllEnableHandler(final boolean terminateInError) {

        final Set<Identity> handlerIdSet = this.mockHandlerManager.registeredHandlerIds();

        final QueryEnableHandlerConditionDto conditionParam = QueryEnableHandlerConditionDto.builder().excludeHandlerIdList(handlerIdSet).build();

        long total = -1;

        do {
            final PageData<MockHandlerDto> pageData = DynamicMockAdminContext.getInstance()
                    .getMockHandlerService()
                    .queryEnableHandlerList(conditionParam, PageParam.of(1, 10));

            if (total < 0) {
                total = pageData.getTotal();
            }

            this.registerHandler(pageData.getList(), terminateInError);

            total -= pageData.getList().size();

        } while (total > 0L);
    }

    @Override
    public synchronized void unRegisterAllDisableHandler(final boolean terminateInError) {

        final Set<Identity> handlerIdSet = this.mockHandlerManager.registeredHandlerIds();

        if (CollectionUtils.isEmpty(handlerIdSet)) {
            return;
        }

        final QueryDisableHandlerIdsConditionDto conditionParamDto = QueryDisableHandlerIdsConditionDto.builder()
                .handlerIdRangeList(handlerIdSet)
                .build();

        long total = -1;

        do {
            final PageData<Identity> pageData = DynamicMockAdminContext.getInstance()
                    .getMockHandlerService()
                    .queryDisableHandlerList(conditionParamDto, PageParam.of(1, 200));

            if (total < 0) {
                total = pageData.getTotal();
            }

            this.unregisterHandler(pageData.getList(), terminateInError);

            total -= pageData.getList().size();

        } while (total > 0L);
    }

    @Override
    public synchronized void refreshRegisteredHandler(final boolean terminateInError) {

        final Map<Identity, Long> handlerVersionMap = this.mockHandlerManager.registeredHandlerVersions();

        final QueryEnableHandlerConditionDto conditionParam = QueryEnableHandlerConditionDto.builder()
                .handlerIdRangeList(handlerVersionMap.keySet())
                .build();

        long total = -1;

        do {
            final PageData<MockHandlerDto> pageData = DynamicMockAdminContext.getInstance()
                    .getMockHandlerService()
                    .queryEnableHandlerList(conditionParam, PageParam.of(1, 20));

            final List<MockHandlerDto> waitRefreshHandlerList = pageData.getList().stream()
                    .filter(handlerInfo -> !((Long) handlerInfo.getVersion()).equals(handlerVersionMap.get(handlerInfo.getHandlerId())))
                    .collect(Collectors.toList());

            if (total < 0) {
                total = pageData.getTotal();
            }

            this.refreshRegisteredHandlers(waitRefreshHandlerList, terminateInError);

            total -= pageData.getList().size();

        } while (total > 0L);

    }

    @Override
    public void registerHandler(final List<MockHandlerDto> mockHandlerDtoList, final boolean terminateInError) {

        final Map<Identity, ProjectDto> projectIdProjectMap = this.findProjectIdProjectMap(mockHandlerDtoList);

        for (final MockHandlerDto mockHandlerDto : mockHandlerDtoList) {
            try {
                this.registerHandler(mockHandlerDto, projectIdProjectMap.get(mockHandlerDto.getProjectId()));
            } catch (Exception ex) {
                log.error("Mock Handler: {} register failed.", mockHandlerDto.getName());
                if (terminateInError) {
                    throw ex;
                }
            }
        }
    }

    /**
     * 注册处理器.mockHandler的项目id必须和project一致
     *
     * @param mockHandler 处理器信息
     * @param project     项目信息
     */
    @Override
    public void registerHandler(final MockHandlerDto mockHandler, final ProjectDto project) {

        CheckUtils.isEquals(mockHandler.getProjectId(), project.getProjectId(), DynamicMockAdminException.supplier(ErrorCode.MOCK_HANDLER_PROJECT_MISMATCH, mockHandler.getProjectId().stringValue(), project.getProjectId().stringValue()));

        MockHandlerDto actualMockHandler;

        if (StringUtils.isNotBlank(project.getBaseUri())) {
            // 在原始的处理地址上添加项目基础uri
            actualMockHandler = MockHandlerDto.copyOf(mockHandler, (handler, builder) -> {
                builder.requestUri(project.getBaseUri() + mockHandler.getRequestUri());
                this.customizeSpaceFileProcessing(mockHandler);
            });
        } else {
            actualMockHandler = MockHandlerDto.copyOf(mockHandler, (handler, builder) -> this.customizeSpaceFileProcessing(mockHandler));
        }

        this.mockHandlerManager.registerHandler(this.mockHandlerFactory.buildMockHandler(actualMockHandler));
    }

    @Override
    public void registerHandler(final MockHandlerDto mockHandlerDto) {

        final ProjectDto projectDto = DynamicMockAdminContext.getInstance()
                .getProjectService()
                .find(mockHandlerDto.getProjectId());

        this.registerHandler(mockHandlerDto, projectDto);
    }

    private void customizeSpaceFileProcessing(final MockHandlerDto mockHandler) {
        final Map<String, Object> customizeSpace = mockHandler.getCustomizeSpace();

        for (final Map.Entry<String, Object> entry : customizeSpace.entrySet()) {
            final Object value = entry.getValue();
            if (value instanceof String) {
                final String valueStr = (String) value;
                if (valueStr.startsWith(FILE_FLAG) && (valueStr.endsWith(".json") || valueStr.endsWith(".txt"))) {
                    final TextFile textFile = this.fileService.find(valueStr.replace(FILE_FLAG, "").trim());
                    CheckUtils.isTrue(JsonUtils.isValidJson(textFile.getContent()), DynamicMockAdminException.supplier(ErrorCode.CONTENT_FORMAT_ERROR));
                    customizeSpace.put(entry.getKey(), JsonUtils.toBean(textFile.getContent()));
                }
            }
        }
    }

    @Override
    public void unregisterHandler(final List<Identity> handlerIdList, final boolean terminateInError) {
        if (CollectionUtils.isEmpty(handlerIdList)) {
            return;
        }

        for (final Identity handlerId : handlerIdList) {
            try {
                this.mockHandlerManager.unregisterHandler(handlerId);
            } catch (Exception ex) {
                log.error("Mock Handler unregister failed. Handler Id: {}", handlerId);
                if (terminateInError) {
                    throw ex;
                }
            }
        }
    }

    @Override
    public void unregisterHandler(final Identity handlerId) {
        if (null == handlerId) {
            return;
        }

        try {
            this.mockHandlerManager.unregisterHandler(handlerId);
        } catch (Exception ex) {
            log.error("Mock Handler unregister failed. Handler Id: {}", handlerId);
        }
    }

    @Override
    public void refreshRegisteredHandlerByProject(final Identity projectId) {

        if (null == projectId) {
            return;
        }

        final QueryEnableHandlerConditionDto conditionParam = QueryEnableHandlerConditionDto.builder().projectId(projectId).build();

        long total = -1;

        do {
            final PageData<MockHandlerDto> pageData = DynamicMockAdminContext.getInstance()
                    .getMockHandlerService()
                    .queryEnableHandlerList(conditionParam, PageParam.of(1, 200));

            if (total < 0) {
                total = pageData.getTotal();
            }

            this.refreshRegisteredHandlers(pageData.getList(), true);

            total -= pageData.getList().size();

        } while (total > 0L);
    }

    @Override
    public void refreshRegisteredHandlers(final List<MockHandlerDto> mockHandlerDtoList, final boolean terminateInError) {

        if (CollectionUtils.isEmpty(mockHandlerDtoList)) {
            return;
        }

        final Map<Identity, ProjectDto> projectIdProjectMap = this.findProjectIdProjectMap(mockHandlerDtoList);

        for (final MockHandlerDto mockHandlerDto : mockHandlerDtoList) {
            try {
                this.unregisterHandler(mockHandlerDto.getHandlerId());
                this.registerHandler(mockHandlerDto, projectIdProjectMap.get(mockHandlerDto.getProjectId()));
            } catch (Exception ex) {
                log.error("Mock Handler refresh failed.", ex);
                if (terminateInError) {
                    // unregisterHandler不会抛出异常,registerHandler抛出异常时不会注册成功,等待定时任务稍后重新加载旧处理器
                    throw ex;
                }
            }
        }
    }

    private Map<Identity, ProjectDto> findProjectIdProjectMap(final List<MockHandlerDto> mockHandlerDtoList) {
        final List<Identity> projectIdList = mockHandlerDtoList.stream().map(MockHandlerDto::getProjectId).distinct().collect(Collectors.toList());
        return DynamicMockAdminContext.getInstance()
                .getProjectService()
                .query(projectIdList, null, PageParam.of(1, projectIdList.size()))
                .getList()
                .stream()
                .collect(Collectors.toMap(ProjectDto::getProjectId, Function.identity(), (v1, v2) -> v2));
    }

}