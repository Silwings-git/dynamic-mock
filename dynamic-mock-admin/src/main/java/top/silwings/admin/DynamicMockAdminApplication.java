package top.silwings.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.repository.MockHandlerRepository;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.MockHandlerManager;
import top.silwings.core.model.dto.MockHandlerDto;

@Slf4j
@SpringBootApplication(scanBasePackages = {"top.silwings.core", "top.silwings.admin"})
public class DynamicMockAdminApplication implements ApplicationRunner {

    private final MockHandlerRepository mockHandlerRepository;

    private final MockHandlerFactory mockHandlerFactory;

    private final MockHandlerManager mockHandlerManager;

    public DynamicMockAdminApplication(final MockHandlerRepository mockHandlerRepository, final MockHandlerFactory mockHandlerFactory, final MockHandlerManager mockHandlerManager) {
        this.mockHandlerRepository = mockHandlerRepository;
        this.mockHandlerFactory = mockHandlerFactory;
        this.mockHandlerManager = mockHandlerManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(DynamicMockAdminApplication.class, args);
    }


    /**
     * 初始化全部启用的MockHandler并注册
     */
    @Override
    public void run(final ApplicationArguments args) {

        long total = -1;

        do {
            final PageData<MockHandlerDto> pageData = this.mockHandlerRepository.queryEnableHandlerList(PageParam.of(1, 200));
            if (total < 0) {
                total = pageData.getTotal();
            }
            pageData.getList().stream().map(this.mockHandlerFactory::buildMockHandler).forEach(this.mockHandlerManager::registerHandler);
            total -= pageData.getList().size();

        } while (total > 0L);

        log.info("Dynamic Mock Handler initialized.");
    }

}
