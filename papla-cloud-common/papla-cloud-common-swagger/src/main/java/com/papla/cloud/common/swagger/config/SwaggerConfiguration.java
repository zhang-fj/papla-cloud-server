package com.papla.cloud.common.swagger.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
@RequiredArgsConstructor
public class SwaggerConfiguration {


    @Bean
    @ConfigurationProperties(prefix = "papla.cloud.sawgger")
    public SwaggerConfigProperties swaggerConfigProperties(){
        return new SwaggerConfigProperties();
    }

    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerConfigProperties().getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(swaggerConfigProperties().getTitle())
            .description("<div style='font-size:14px;color:red;'>"+swaggerConfigProperties().getDescription()+"</div>")
            .termsOfServiceUrl("https://loclahost:9000/papla")
            .contact(new Contact("张福江", "", ""))
            .license("Open Source")
            .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
            .version("1.0.0")
            .build();
    }

}
