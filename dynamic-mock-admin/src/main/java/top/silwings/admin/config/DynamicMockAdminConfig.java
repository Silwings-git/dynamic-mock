package top.silwings.admin.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import top.silwings.admin.interceptors.UserInterceptor;

/**
 * @ClassName DynamicMockConfig
 * @Description 配置类
 * @Author Silwings
 * @Date 2022/11/13 12:02
 * @Since
 **/
@Configuration
public class DynamicMockAdminConfig implements WebMvcConfigurer {

    @Value("${project.version}")
    private String projectVersion;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Dynamic-Mock Restful API")
                .description("Dynamic-Mock restful api")
                .termsOfServiceUrl("https://gitee.com/silwings")
                .contact(new Contact("Silwings", "https://gitee.com/silwings", "silwings@163.com"))
                .version(this.projectVersion)
                .build();
    }

    @Bean
    public Docket createRestApi(final ApiInfo apiInfo) {
        return new Docket(DocumentationType.SPRING_WEB)
                .apiInfo(apiInfo)
                .groupName("core")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

}