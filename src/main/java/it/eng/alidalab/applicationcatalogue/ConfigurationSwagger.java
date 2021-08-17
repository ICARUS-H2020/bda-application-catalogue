package it.eng.alidalab.applicationcatalogue;


import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@org.springframework.context.annotation.Configuration
@EnableSwagger2
public class ConfigurationSwagger {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))//<6>, regex must be in double quotes.
                .build()
                .apiInfo(apiInfo());
    }


    private ApiInfo apiInfo() {
        return new ApiInfo(
                "BDA Catalogue API",
                "This is the UI to use REST API of the BDA Catalogue.",
                "v1",
                "Terms of service",
                new Contact("Alida Teams", "https://home.alidalab.it/", "info@alidalab.it"),
                "License of API", "API license URL", Collections.emptyList());
    }


}


