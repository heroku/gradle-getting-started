package com.deliverit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Lists.newArrayList;


@EnableSwagger2
@Configuration
public class Swagger2Config {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(newArrayList(new ParameterBuilder()
                        .name("Authorization")
                        .description("Email address")
                        .modelRef(new ModelRef("String"))
                        .parameterType("header")
                        .required(false)
                        .build()))
                .apiInfo(apiInfo());
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("DeliverIt API Documentation")
                .description(description())
                .version("1.0")
                .build();
    }

    private String description(){
        return "Welcome to DeliverIT Documentation. The DeliverIT API allows you to do stuff. \n" +
                "See also at: https://github.com/LyubomirDenkov/DeliverIt-documentation";
    }

}
