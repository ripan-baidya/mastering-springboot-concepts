package com.springboot.graphqlclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * This configuration class is used to create a {@link HttpGraphQlClient} bean.
 * The {@link HttpGraphQlClient} is a Spring GraphQL client that can be used to make GraphQL queries to a GraphQL server.
 * It uses the {@link WebClient} under the hood to make the actual HTTP requests to the GraphQL server.
 * The {@link WebClient} is configured with a base URL of "http://localhost:8080/graphql" which is the URL of the GraphQL server.
 * The {@link HttpGraphQlClient} is then built with the configured {@link WebClient} and returned as a Spring bean.
 */
@Configuration
public class GraphQlClientConfig {

    /**
     * This method creates and returns a {@link HttpGraphQlClient} bean.
     * The {@link HttpGraphQlClient} is a Spring GraphQL client that can be used to make GraphQL queries to a GraphQL server.
     * It is configured with a base URL of "http://localhost:8080/graphql" which is the URL of the GraphQL server.
     * The {@link HttpGraphQlClient} is then built with the configured {@link WebClient} and returned as a Spring bean.
     * @return The {@link HttpGraphQlClient} bean.
     */
    @Bean
    public HttpGraphQlClient graphQlClient() {
        /**
         * This is the base URL of the GraphQL server.
         * It is used to make the actual HTTP requests to the GraphQL server.
         */
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/graphql")
                .build();

        /**
         * This is the actual {@link HttpGraphQlClient} that is built with the configured {@link WebClient}.
         * It is then returned as a Spring bean.
         */
        return HttpGraphQlClient.builder(webClient).build();
    }
}