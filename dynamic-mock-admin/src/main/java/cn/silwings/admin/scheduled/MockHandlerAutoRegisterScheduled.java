package cn.silwings.admin.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import cn.silwings.admin.service.MockHandlerRegisterService;

/**
 * @ClassName MockHandlerAutoRegisterScheduled
 * @Description 任务自动注册定时任务.用于解决多节点任务状态同步
 * @Author Silwings
 * @Date 2023/1/5 0:21
 * @Since
 **/
@Component
public class MockHandlerAutoRegisterScheduled {

    private final MockHandlerRegisterService mockHandlerRegisterService;

    public MockHandlerAutoRegisterScheduled(final MockHandlerRegisterService mockHandlerRegisterService) {
        this.mockHandlerRegisterService = mockHandlerRegisterService;
    }

    @Scheduled(cron = "${dynamic-mock.admin.sync-handler.register-cron:0 0/1 * * * ?}")
    public void autoRegister() {
        this.mockHandlerRegisterService.registerAllEnableHandler(false);
    }

    @Scheduled(cron = "${dynamic-mock.admin.sync-handler.un-register-cron:0 0/1 * * * ?}")
    public void autoUnRegister() {
        this.mockHandlerRegisterService.unRegisterAllDisableHandler(false);
    }

    @Scheduled(cron = "${dynamic-mock.admin.sync-handler.refresh-cron:0 0/1 * * * ?}")
    public void autoRefresh() {
        this.mockHandlerRegisterService.refreshRegisteredHandler(false);
    }

}