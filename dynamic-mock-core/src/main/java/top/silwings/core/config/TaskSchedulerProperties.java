package top.silwings.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @ClassName TaskSchedulerProperties
 * @Description TaskSchedulerProperties
 * @Author Silwings
 * @Date 2022/1/9 11:24
 * @Version V1.0
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "mock.task.scheduler")
@PropertySource({"classpath:application.yml"})
public class TaskSchedulerProperties {

    /**
     * 核心线程数
     */
    private int corePoolSize = 20;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 50;

    /**
     * 等待队列容量
     */
    private int queueCapacity = 200;

}
