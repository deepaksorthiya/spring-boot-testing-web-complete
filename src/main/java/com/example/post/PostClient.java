package com.example.post;


import com.example.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@EnableConfigurationProperties(ApplicationProperties.class)
public class PostClient {

    private final RestClient restClient;
    private final ApplicationProperties.PostProperties postProperties;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostClient.class);

    public PostClient(RestClient.Builder builder, ApplicationProperties appProperties) {
        this.postProperties = appProperties.post();
        this.restClient = builder.baseUrl(this.postProperties.api().baseUrl()).build();
    }

    public Post getPost(Integer id) {
        LOGGER.info("PostProperties {}", this.postProperties);
        return this.restClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .body(Post.class);
    }
}
