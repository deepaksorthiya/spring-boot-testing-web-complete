package com.example.post;

import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostClient postClient;

    public PostService(PostClient postClient) {
        this.postClient = postClient;
    }

    public Post getPost(Integer id) {
        return postClient.getPost(id);
    }
}
