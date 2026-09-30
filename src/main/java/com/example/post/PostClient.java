package com.example.post;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PostClient {

    private final RestClient restClient;

    public PostClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    public Post getPost(Integer id) {
        return this.restClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .body(Post.class);
    }
}
