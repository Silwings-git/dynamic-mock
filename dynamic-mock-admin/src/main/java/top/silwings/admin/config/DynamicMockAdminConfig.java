package top.silwings.admin.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import top.silwings.admin.auth.interceptors.UserInterceptor;
import top.silwings.admin.auth.interceptors.WebContextInterceptor;
import top.silwings.admin.service.LoginService;

/**
 * @ClassName DynamicMockConfig
 * @Description 配置类
 * @Author Silwings
 * @Date 2022/11/13 12:02
 * @Since
 **/
@Configuration
@Import(value = GlobalCorsProperties.class)
public class DynamicMockAdminConfig implements WebMvcConfigurer {

    @Value("${project.version}")
    private String projectVersion;

    private final LoginService loginService;

    public DynamicMockAdminConfig(final LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new WebContextInterceptor());
        registry.addInterceptor(new UserInterceptor(this.loginService));
    }

    @Bean
    public CorsFilter corsFilter(final GlobalCorsProperties globalCorsProperties) {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.setCorsConfigurations(globalCorsProperties.getCorsConfigurations());
        return new CorsFilter(source);
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("Dynamic-Mock Restful API")
                        .description("Dynamic-Mock restful api")
                        .termsOfServiceUrl("https://gitee.com/silwings")
                        .contact(new Contact("Silwings", "https://gitee.com/silwings", "silwings@163.com"))
                        .version(this.projectVersion)
                        .build())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

}