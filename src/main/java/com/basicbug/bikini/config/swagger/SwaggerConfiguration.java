package com.basicbug.bikini.config.swagger;

import static com.basicbug.bikini.config.swagger.SwaggerConstants.AUTH;
import static com.basicbug.bikini.config.swagger.SwaggerConstants.COMMON;
import static com.basicbug.bikini.config.swagger.SwaggerConstants.FEED;
import static com.basicbug.bikini.config.swagger.SwaggerConstants.USER;
import static com.basicbug.bikini.config.swagger.SwaggerConstants.VERSION_1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static final String CONTROLLER_BASE_PATH = "com.basicbug.bikini.controller";
    private static final String GROUP_URL_PATTERN = "/%s/%s/**";

    @Bean
    public Docket feedApiDocket() {
        return createDocket(FEED, String.format(GROUP_URL_PATTERN, VERSION_1, FEED), VERSION_1);
    }

    @Bean
    public Docket authApiDocket() {
        return createDocket(AUTH, String.format(GROUP_URL_PATTERN, VERSION_1, AUTH), VERSION_1);
    }

    @Bean
    public Docket commonApiDocket() {
        return createDocket(COMMON, String.format(GROUP_URL_PATTERN, VERSION_1, COMMON), VERSION_1);
    }

    @Bean
    public Docket userApiDocket() {
        return createDocket(USER, String.format(GROUP_URL_PATTERN, VERSION_1, USER), VERSION_1);
    }

    private Docket createDocket(String groupName, String groupUrl, String version) {
        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .groupName(groupName)
            .select()
            .apis(RequestHandlerSelectors.basePackage(CONTROLLER_BASE_PATH))
            .paths(PathSelectors.ant(groupUrl))
            .build()
            .apiInfo(apiInfo(groupName + "-" + version, version));
    }

    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfoBuilder()
            .title(title)
            .description("BasicBug Bikini API Docs")
            .license("BasicBug")
            .version(version)
            .build();
    }
}
