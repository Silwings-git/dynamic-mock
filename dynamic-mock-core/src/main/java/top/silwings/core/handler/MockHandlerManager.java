package top.silwings.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.common.PageData;
import top.silwings.core.common.PageParam;
import top.silwings.core.exceptions.NoMockHandlerFoundException;
import top.silwings.core.repository.MockHandlerRepository;
import top.silwings.core.repository.dto.MockHandlerDto;
import top.silwings.core.repository.dto.QueryConditionDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName MockHandlerManager
 * @Description MockHandler管理器
 * @Author Silwings
 * @Date 2022/11/10 21:49
 * @Since
 **/
@Slf4j
@Component
public class MockHandlerManager implements ApplicationRunner {

    private final Map<Identity, MockHandler> handlerMap;

    private final MockHandlerFactory mockHandlerFactory;

    private final MockHandlerRepository mockHandlerRepository;

    public MockHandlerManager(final MockHandlerFactory mockHandlerFactory, final MockHandlerRepository mockHandlerRepository) {
        this.mockHandlerFactory = mockHandlerFactory;
        this.mockHandlerRepository = mockHandlerRepository;
        this.handlerMap = new ConcurrentHashMap<>();
    }

    public void registerHandler(final MockHandler mockHandler) {
        this.handlerMap.put(mockHandler.getHandlerId(), mockHandler);
        log.info("Mock Handler {} registered.", mockHandler.getName());
    }

    public void unregisterHandler(final Identity handlerId) {
        this.handlerMap.remove(handlerId);
    }

    private MockHandler filter(final RequestInfo requestInfo) {

        for (final MockHandler mockHandler : this.handlerMap.values()) {
            if (mockHandler.support(requestInfo)) {
                return mockHandler;
            }
        }

        throw new NoMockHandlerFoundException(requestInfo);
    }

    public ResponseEntity<Object> mock(final Context context) {

        final MockHandler mockHandler = this.filter(RequestInfo.from(context));

        return mockHandler.delay().mock(context);
    }

    /**
     * 初始化全部启用的MockHandler并注册
     */
    @Override
    public void run(final ApplicationArguments args) {

        long total = -1;

        do {
            final PageData<MockHandlerDto> pageData = this.mockHandlerRepository.query(QueryConditionDto.builder().enableStatus(EnableStatus.ENABLE).build(), PageParam.of(1, 200));
            if (total < 0) {
                total = pageData.getTotal();
            }
            pageData.getList().stream().map(this.mockHandlerFactory::buildMockHandler).forEach(this::registerHandler);
            total -= pageData.getList().size();

        } while (total > 0L);
    }

}