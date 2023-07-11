package top.silwings.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName MockTaskHttpProperties
 * @Description Mock任务的http客户端配置
 * @Author Silwings
 * @Date 2023/7/11 21:32
 * @Since
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "dynamic-mock.task.http")
public class MockTaskHttpProperties {

    /**
     * 响应超时时间(读取超时时间)
     */
    private int responseTimeout = 10_000;

    /**
     * 连接超时时间
     */
    private int connectTimeout = 5_000;

}