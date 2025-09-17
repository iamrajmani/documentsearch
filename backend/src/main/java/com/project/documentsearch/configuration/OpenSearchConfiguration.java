package com.project.documentsearch.configuration;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfiguration {

    @Bean(destroyMethod = "close")
    public RestHighLevelClient openSearchClient() {
        HttpHost host = new HttpHost("localhost", 9200, "http");
        return new RestHighLevelClient(RestClient.builder(host));
    }
}

