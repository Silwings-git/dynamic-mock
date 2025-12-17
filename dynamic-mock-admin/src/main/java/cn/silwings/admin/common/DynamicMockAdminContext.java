package cn.silwings.admin.common;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import cn.silwings.admin.service.MockHandlerRegisterService;
import cn.silwings.admin.service.MockHandlerService;
import cn.silwings.admin.service.ProjectService;

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

    private final MockHandlerRegisterService mockHandlerRegisterService;

    public DynamicMockAdminContext(final MockHandlerService mockHandlerService, final ProjectService projectService, final MockHandlerRegisterService mockHandlerRegisterService) {
        this.mockHandlerService = mockHandlerService;
        this.projectService = projectService;
        this.mockHandlerRegisterService = mockHandlerRegisterService;
    }

    public static DynamicMockAdminContext getInstance() {
        return DYNAMIC_MOCK_ADMIN_CONTEXT;
    }

    @Override
    public void afterPropertiesSet() {
        DYNAMIC_MOCK_ADMIN_CONTEXT = this;
    }

}