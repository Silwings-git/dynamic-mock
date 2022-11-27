package top.silwings.admin.config;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.admin.service.ProjectService;

/**
 * @ClassName DynamicMockContext
 * @Description 全局上下文
 * @Author Silwings
 * @Date 2022/11/23 22:43
 * @Since
 **/
@Getter
@Component
public class DynamicMockAdminContext implements InitializingBean {

    private static DynamicMockAdminContext DYNAMIC_MOCK_ADMIN_CONTEXT = null;

    private final MockHandlerService mockHandlerService;

    private final ProjectService projectService;

    public DynamicMockAdminContext(final MockHandlerService mockHandlerService, final ProjectService projectService) {
        this.mockHandlerService = mockHandlerService;
        this.projectService = projectService;
    }

    public static DynamicMockAdminContext getInstance() {
        return DYNAMIC_MOCK_ADMIN_CONTEXT;
    }

    @Override
    public void afterPropertiesSet() {
        DYNAMIC_MOCK_ADMIN_CONTEXT = this;
    }

}