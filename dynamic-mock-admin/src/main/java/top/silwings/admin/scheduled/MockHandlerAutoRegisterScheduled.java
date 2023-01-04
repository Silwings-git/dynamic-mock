package top.silwings.admin.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName MockHandlerAutoRegisterScheduled
 * @Description 任务自动注册定时任务.用于解决多节点任务状态同步
 * @Author Silwings
 * @Date 2023/1/5 0:21
 * @Since
 **/
@Component
public class MockHandlerAutoRegisterScheduled {

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void autoRegister() {

        // TODO_Silwings: 2023/1/5
        // 实现自动注册
        // 如果数据库为启用状态,当前处理器管理器中未注册,执行注册

    }

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void autoUnRegister() {

        // TODO_Silwings: 2023/1/5
        // 实现取消注册
        // 如果处理器管理器中的任务在数据库中为取消注册状态或不存在,自动取消注册

    }

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void autoReRegister() {

        // TODO_Silwings: 2023/1/5
        // 实现重新注册
        // 如果处理器管理器中的任务在数据库中为启用,但最后更新时间不一致,进行重新注册.
        // 考虑合并到自动注册中

    }

}