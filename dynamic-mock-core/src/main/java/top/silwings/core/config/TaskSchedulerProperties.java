package top.silwings.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName TaskSchedulerProperties
 * @Description TaskSchedulerProperties
 * @Author Silwings
 * @Date 2022/1/9 11:24
 * @Version V1.0
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "dynamic-mock.task.scheduler")
public class TaskSchedulerProperties {

    /**
     * 核心线程数
     */
    private int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 最大任务池容量
     */
    private int maxTaskPoolSize = 1000;

}
