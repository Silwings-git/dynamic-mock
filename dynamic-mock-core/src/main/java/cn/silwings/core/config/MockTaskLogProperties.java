package cn.silwings.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName MockTaskLogProperties
 * @Description 任务日志配置文件
 * @Author Silwings
 * @Date 2022/11/24 1:40
 * @Since
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "dynamic-mock.task.log")
public class MockTaskLogProperties {

    /**
     * 是否记录Task请求信息
     */
    private boolean enableLog = true;

    /**
     * 是否记录请求信息
     */
    private boolean logRequestInfo = true;

    /**
     * 是否记录请求信息
     */
    private boolean logResponse = true;

    /**
     * 是否记录响应状态码
     */
    private boolean logHttpStatus = true;

    /**
     * 是否记录响应头
     */
    private boolean logResponseHeaders = false;

    /**
     * 是否记录响应体
     */
    private boolean logResponseBody = true;

}