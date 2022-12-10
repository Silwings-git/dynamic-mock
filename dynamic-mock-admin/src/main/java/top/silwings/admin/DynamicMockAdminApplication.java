package top.silwings.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.model.ProjectDto;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.admin.service.ProjectService;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockHandlerDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableSwagger2
@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"top.silwings.core", "top.silwings.admin"})
public class DynamicMockAdminApplication implements ApplicationRunner {

    private final MockHandlerService mockHandlerService;

    private final ProjectService projectService;

    public DynamicMockAdminApplication(final MockHandlerService mockHandlerService, final ProjectService projectService) {
        this.mockHandlerService = mockHandlerService;
        this.projectService = projectService;
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

        final Map<Identity, ProjectDto> projectMap = new HashMap<>();

        do {
            final PageData<MockHandlerDto> pageData = this.mockHandlerService.queryEnableHandlerList(PageParam.of(1, 200));

            pageData.getList().stream()
                    .map(MockHandlerDto::getProjectId)
                    .forEach(projectId -> {
                        if (!projectMap.containsKey(projectId)) {
                            projectMap.put(projectId, this.projectService.find(projectId));
                        }
                    });

            if (total < 0) {
                total = pageData.getTotal();
            }

            pageData.getList().forEach(handler -> this.mockHandlerService.registerHandler(handler, projectMap.get(handler.getProjectId())));

            total -= pageData.getList().size();

        } while (total > 0L);

        log.info("Dynamic Mock Handler initialized.");
    }

}
