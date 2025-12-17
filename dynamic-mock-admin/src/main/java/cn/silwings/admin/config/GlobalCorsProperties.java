package cn.silwings.admin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName GlobalCorsProperties
 * @Description 全局跨域配置
 * @Author Silwings
 * @Date 2022/11/26 20:47
 * @Since
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "dynamic-mock.admin.cors")
public class GlobalCorsProperties {

    private final Map<String, CorsConfiguration> corsConfigurations = new LinkedHashMap<>();

    public Map<String, CorsConfiguration> getCorsConfigurations() {
        return this.corsConfigurations;
    }

}