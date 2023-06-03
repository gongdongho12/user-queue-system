package com.dongholab.uqs.api.configuration

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun dongholabApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("dongholab")
            .pathsToMatch("/**")
            .pathsToExclude("/actuator/**")
            .build()
    }

    @Bean
    fun acturatorApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("actuator")
            .pathsToMatch("/actuator/**")
            .build()
    }

    @Bean
    fun dongholabInfo(): OpenAPI {
        val dongholabInfo = Info().title("Dongholab API").description("generated by gongdongho12")
        val dongholabBlog = ExternalDocumentation().description("Dongholab Blog")
            .url("https://blog.dongholab.com")
        val graphqlPlayground = ExternalDocumentation().description("Graphql Playground")
            .url("/graphiql")
        return OpenAPI()
            .info(dongholabInfo)
            .externalDocs(dongholabBlog)
            .externalDocs(graphqlPlayground)
    }
}